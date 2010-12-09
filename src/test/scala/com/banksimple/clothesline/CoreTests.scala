package com.banksimple.clothesline

import clothesline._
import clothesline.service._
import clothesline.interop.nodetest._
import org.specs._
import clojure.lang._
import com.codahale.yoink.{PersistentHashMap => PMap}

import Util._

class ExampleService extends Service {
  override def contentTypesProvided(request: IPersistentMap, graphData: IPersistentMap): TestResult = {
    responders(
      "text/plain" -> responder("this is a test")
      )
  }
  override def resourceExists(request: IPersistentMap, graphData: IPersistentMap) = {
    result(true)
  }
}

class CoreSpec extends Specification {
  val eg = new ExampleService()
  val ps = (PMap() + ("foo" -> "bar")).underlying
  val request = (PMap() + (keyword("params") -> ps)).underlying
  val graphData = PMap().underlying

  "Should be able to get request params as Parameters" in {
    import Parameters._
    val p = Parameters.fromRequest(request)
    p("foo") must haveClass[String]
    p("foo") must be_==("bar")
    p.get("foo") must beSome[String]
    p.get("foo") must beSome("bar")
  }

  "Should be able to pimp parameters" in {
    import Parameters._
    var p: APersistentMap = PersistentHashMap.EMPTY
    p = p.assoc("foo","bar").asInstanceOf[APersistentMap]
    val params = Parameters(p)
    params("foo") must haveClass[String]
    params("foo") must be_==("bar")
    params.get("foo") must beSome[String]
    params.get("foo") must beSome("bar")
  }

  "Ensure that we can create a base service" in {
    eg must haveSuperClass[Service]
    val testResult = eg.contentTypesProvided(request, graphData)
    testResult must haveClass[TestResult]
    val result = testResult.result.asInstanceOf[APersistentMap]
    val annotations = testResult.annotations.asInstanceOf[APersistentMap]
  }

  "Assure responders are getting set correctly" in {
    val testResult = eg.contentTypesProvided(request, graphData)
    val resultMap = testResult.result.asInstanceOf[APersistentMap]
    val results = new PMap[String, AFn](resultMap)
    results("text/plain").invoke(null, null) must be_==("this is a test")
  }
}

class RichResultSpec extends Specification {

  "RichTestResult" should {
    "accept annotations" in  {
      val richResult = RichTestResult(true)
      val annotated  = richResult.annotate("x" -> "Word")
      val annMap = annotated.annotations
      val pendingAnnMap = annMap(keyword("annotate"))

      pendingAnnMap.count() must be_==( 1 )
      pendingAnnMap must havePair(keyword('x) -> "Word")
    }

    "accept headers" in {
      import Util._
      val headerizedRichResult = RichTestResult("hey") header( "X-Poop", "true" )
      val annMap = headerizedRichResult.annotations

      annMap must haveKey(keyword("headers"))
      annMap(keyword("headers")) must havePair("X-Poop", "true")
    }
  }
}
