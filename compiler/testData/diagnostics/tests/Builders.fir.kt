// RUN_PIPELINE_TILL: BACKEND
// FILE: a.kt

/**
 * This is an example of a Type-Safe Groovy-style Builder
 *
 * Builders are good for declaratively describing data in your code.
 * In this example we show how to describe an HTML page in Kotlin.
 *
 * See this page for details:
 * http://confluence.jetbrains.net/display/Kotlin/Type-safe+Groovy-style+builders
 */
package html

import java.util.*

fun main(args : Array<String>) {
  val result =
    html {
      head {
        title {+"XML encoding with Kotlin"}
      }
      body {
        h1 {+"XML encoding with Kotlin"}
        p {+"this format can be used as an alternative markup to XML"}

        // an element with attributes and text content
        a(href = "https://jetbrains.com/kotlin") {+"Kotlin"}

        // mixed content
        p {
          +"This is some"
          b {+"mixed"}
          +"text. For more see the"
          a(href = "https://jetbrains.com/kotlin") {+"Kotlin"}
          +"project"
        }
        p {+"some text"}

        // content generated from command-line arguments
        p {
          +"Command line arguments were:"
          ul {
            for (arg in args)
              li {+arg}
          }
        }
      }
    }
  println(result)
}

abstract class Element {
  abstract fun render(builder : StringBuilder, indent : String)

  override fun toString() : String {
    val builder = StringBuilder()
    render(builder, "")
    return builder.toString()
  }
}

class TextElement(val text : String) : Element() {
  override fun render(builder : StringBuilder, indent : String) {
    builder.append("$indent$text\n")
  }
}

abstract class Tag(val name : String) : Element() {
  val children = ArrayList<Element>()
  val attributes = HashMap<String, String>()

  protected fun <T : Element> initTag(tag : T, init : T.() -> Unit) : T {
    tag.init()
    children.add(tag)
    return tag
  }

  override fun render(builder : StringBuilder, indent : String) {
    builder.append("$indent<$name${renderAttributes()}>\n")
    for (c in children) {
      c.render(builder, indent + "  ")
    }
    builder.append("$indent</$name>\n")
  }

  private fun renderAttributes() : String? {
    val builder = StringBuilder()
    for (a in attributes.keys) {
      builder.append(" $a=\"${attributes[a]}\"")
    }
    return builder.toString()
  }
}

abstract class TagWithText(name : String) : Tag(name) {
  operator fun String.unaryPlus() {
    children.add(TextElement(this))
  }
}

class HTML() : TagWithText("html") {
  fun head(init : Head.() -> Unit) = initTag(Head(), init)

  fun body(init : Body.() -> Unit) = initTag(Body(), init)
}

class Head() : TagWithText("head") {
  fun title(init : Title.() -> Unit) = initTag(Title(), init)
}

class Title() : TagWithText("title")

abstract class BodyTag(name : String) : TagWithText(name) {
  fun b(init : B.() -> Unit) = initTag(B(), init)
  fun p(init : P.() -> Unit) = initTag(P(), init)
  fun h1(init : H1.() -> Unit) = initTag(H1(), init)
  fun ul(init : UL.() -> Unit) = initTag(UL(), init)
  fun a(href : String, init : A.() -> Unit) {
    val a = initTag(A(), init)
    a.href = href
  }
}

class Body() : BodyTag("body")
class UL() : BodyTag("ul") {
  fun li(init : LI.() -> Unit) = initTag(LI(), init)
}

class B() : BodyTag("b")
class LI() : BodyTag("li")
class P() : BodyTag("p")
class H1() : BodyTag("h1")
class A() : BodyTag("a") {
  public var href : String?
    get() = attributes["href"]
    set(value) {
       if (value != null) {
           attributes.put("href", value)
//         attributes["href"] = value //doesn't work: KT-1355
       }
    }
}

fun html(init : HTML.() -> Unit) : HTML {
  val html = HTML()
  html.init()
  return html
}

// An excerpt from the Standard Library
operator fun <K, V> MutableMap<K, V>.set(key : K, value : V) = this.put(key, value)

fun println(message : Any?) {
  System.out.println(message)
}

fun print(message : Any?) {
  System.out.print(message)
}

/* GENERATED_FIR_TAGS: additiveExpression, assignment, classDeclaration, equalityExpression, flexibleType, forLoop,
funWithExtensionReceiver, functionDeclaration, functionalType, getter, ifExpression, javaFunction, javaProperty,
lambdaLiteral, localProperty, nullableType, operator, override, primaryConstructor, propertyDeclaration, setter,
smartcast, stringLiteral, thisExpression, typeConstraint, typeParameter, typeWithExtension, unaryExpression */
