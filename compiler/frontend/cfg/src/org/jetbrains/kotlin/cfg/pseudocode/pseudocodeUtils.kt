/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.cfg.pseudocode

import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.cfg.Label
import org.jetbrains.kotlin.cfg.containingDeclarationForPseudocode
import org.jetbrains.kotlin.cfg.pseudocode.instructions.Instruction
import org.jetbrains.kotlin.cfg.pseudocode.instructions.eval.*
import org.jetbrains.kotlin.cfg.pseudocode.instructions.jumps.ConditionalJumpInstruction
import org.jetbrains.kotlin.cfg.pseudocode.instructions.jumps.ReturnValueInstruction
import org.jetbrains.kotlin.cfg.pseudocode.instructions.jumps.ThrowExceptionInstruction
import org.jetbrains.kotlin.config.LanguageVersionSettingsImpl
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.impl.LocalVariableDescriptor
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType
import org.jetbrains.kotlin.psi.psiUtil.parents
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getTargetFunctionDescriptor
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.calls.model.VarargValueArgument
import org.jetbrains.kotlin.resolve.calls.util.getExplicitReceiverValue
import org.jetbrains.kotlin.resolve.calls.util.isSafeCall
import org.jetbrains.kotlin.resolve.findTopMostOverriddenDescriptors
import org.jetbrains.kotlin.resolve.scopes.receivers.ReceiverValue
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeUtils
import java.util.ArrayList
import java.util.LinkedHashSet

fun getReceiverTypePredicate(resolvedCall: ResolvedCall<*>, receiverValue: ReceiverValue): TypePredicate? {
    val callableDescriptor = resolvedCall.resultingDescriptor

    when (receiverValue) {
        resolvedCall.extensionReceiver -> {
            val receiverParameter = callableDescriptor.extensionReceiverParameter
            if (receiverParameter != null) return receiverParameter.type.getSubtypesPredicate()
        }
        resolvedCall.dispatchReceiver -> {
            val rootCallableDescriptors = callableDescriptor.findTopMostOverriddenDescriptors()
            return or(rootCallableDescriptors.mapNotNull {
                it.dispatchReceiverParameter?.type?.let { TypeUtils.makeNullableIfNeeded(it, resolvedCall.call.isSafeCall()) }
                    ?.getSubtypesPredicate()
            })
        }
    }

    return null
}

fun getExpectedTypePredicate(
    value: PseudoValue,
    bindingContext: BindingContext,
    builtIns: KotlinBuiltIns
): TypePredicate {
    val pseudocode = value.createdAt?.owner ?: return AllTypes
    val typePredicates = LinkedHashSet<TypePredicate?>()

    fun addSubtypesOf(kotlinType: KotlinType?) = typePredicates.add(kotlinType?.getSubtypesPredicate())

    fun addByExplicitReceiver(resolvedCall: ResolvedCall<*>?) {
        val receiverValue = (resolvedCall ?: return).getExplicitReceiverValue()
        if (receiverValue != null) typePredicates.add(getReceiverTypePredicate(resolvedCall, receiverValue))
    }

    fun addTypePredicates(value: PseudoValue) {
        pseudocode.getUsages(value).forEach {
            when (it) {
                is ReturnValueInstruction -> {
                    val returnElement = it.element
                    val functionDescriptor = when (returnElement) {
                        is KtReturnExpression -> returnElement.getTargetFunctionDescriptor(bindingContext)
                        else -> bindingContext[BindingContext.DECLARATION_TO_DESCRIPTOR, pseudocode.correspondingElement]
                    }
                    addSubtypesOf((functionDescriptor as? CallableDescriptor)?.returnType)
                }

                is ConditionalJumpInstruction ->
                    addSubtypesOf(builtIns.booleanType)

                is ThrowExceptionInstruction ->
                    addSubtypesOf(builtIns.throwable.defaultType)

                is MergeInstruction ->
                    addTypePredicates(it.outputValue)

                is AccessValueInstruction -> {
                    val accessTarget = it.target
                    val receiverValue = it.receiverValues[value]
                    if (receiverValue != null) {
                        typePredicates.add(getReceiverTypePredicate((accessTarget as AccessTarget.Call).resolvedCall, receiverValue))
                    } else {
                        val expectedType = when (accessTarget) {
                            is AccessTarget.Call ->
                                (accessTarget.resolvedCall.resultingDescriptor as? VariableDescriptor)?.type
                            is AccessTarget.Declaration ->
                                accessTarget.descriptor.type
                            else ->
                                null
                        }
                        addSubtypesOf(expectedType)
                    }
                }

                is CallInstruction -> {
                    val receiverValue = it.receiverValues[value]
                    if (receiverValue != null) {
                        typePredicates.add(getReceiverTypePredicate(it.resolvedCall, receiverValue))
                    } else {
                        it.arguments[value]?.let { parameter ->
                            val expectedType = when (it.resolvedCall.valueArguments[parameter]) {
                                is VarargValueArgument ->
                                    parameter.varargElementType
                                else ->
                                    parameter.type
                            }
                            addSubtypesOf(expectedType)
                        }
                    }
                }

                is MagicInstruction -> when (it.kind) {
                    MagicKind.AND, MagicKind.OR ->
                        addSubtypesOf(builtIns.booleanType)

                    MagicKind.LOOP_RANGE_ITERATION ->
                        addByExplicitReceiver(bindingContext[BindingContext.LOOP_RANGE_ITERATOR_RESOLVED_CALL, value.element as? KtExpression])

                    MagicKind.VALUE_CONSUMER -> {
                        val element = it.element
                        when (element) {
                            element.getStrictParentOfType<KtWhileExpression>()?.condition -> addSubtypesOf(builtIns.booleanType)
                            is KtProperty -> {
                                val propertyDescriptor = bindingContext[BindingContext.DECLARATION_TO_DESCRIPTOR, element] as? PropertyDescriptor
                                propertyDescriptor?.accessors?.map {
                                    addByExplicitReceiver(bindingContext[BindingContext.DELEGATED_PROPERTY_RESOLVED_CALL, it])
                                }
                            }
                            is KtDelegatedSuperTypeEntry -> addSubtypesOf(bindingContext[BindingContext.TYPE, element.typeReference])
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    addTypePredicates(value)
    return and(typePredicates.filterNotNull())
}

fun Instruction.getPrimaryDeclarationDescriptorIfAny(bindingContext: BindingContext): DeclarationDescriptor? {
    return when (this) {
        is CallInstruction -> return resolvedCall.resultingDescriptor
        else -> PseudocodeUtil.extractVariableDescriptorIfAny(this, bindingContext)
    }
}

val Instruction.sideEffectFree: Boolean
    get() = owner.isSideEffectFree(this)

fun Instruction.calcSideEffectFree(): Boolean {
    if (this !is InstructionWithValue) return false
    if (!inputValues.all { it.createdAt?.sideEffectFree == true }) return false

    return when (this) {
        is ReadValueInstruction -> target.let {
            when (it) {
                is AccessTarget.Call -> when (it.resolvedCall.resultingDescriptor) {
                    is LocalVariableDescriptor, is ValueParameterDescriptor, is ReceiverParameterDescriptor -> true
                    else -> false
                }

                else -> when (element) {
                    is KtNamedFunction -> element.name == null
                    is KtConstantExpression, is KtLambdaExpression, is KtStringTemplateExpression -> true
                    else -> false
                }
            }
        }

        is MagicInstruction -> kind.sideEffectFree

        else -> false
    }
}

fun Pseudocode.getElementValuesRecursively(element: KtElement): List<PseudoValue> {
    val results = ArrayList<PseudoValue>()

    fun Pseudocode.collectValues() {
        getElementValue(element)?.let { results.add(it) }
        for (localFunction in localDeclarations) {
            localFunction.body.collectValues()
        }
    }

    collectValues()
    return results
}

fun KtDeclaration.getContainingPseudocode(context: BindingContext): Pseudocode? {
    val enclosingPseudocodeDeclaration = (this as? KtFunctionLiteral)?.let {
        it.parents.firstOrNull { it is KtDeclaration && it !is KtFunctionLiteral } as? KtDeclaration
    } ?: this

    val enclosingPseudocode = PseudocodeUtil.generatePseudocode(enclosingPseudocodeDeclaration, context, LanguageVersionSettingsImpl.DEFAULT)
    return enclosingPseudocode.getPseudocodeByElement(this)
}

fun KtElement.getContainingPseudocode(context: BindingContext) = containingDeclarationForPseudocode?.getContainingPseudocode(context)

fun Pseudocode.getPseudocodeByElement(element: KtElement): Pseudocode? {
    if (correspondingElement == element) return this

    localDeclarations.forEach { decl -> decl.body.getPseudocodeByElement(element)?.let { return it } }
    return null
}

val Label.isJumpToError: Boolean
    get() = resolveToInstruction() == pseudocode.errorInstruction
