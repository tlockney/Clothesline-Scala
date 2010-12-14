package com.banksimple.clothesline

import clothesline._
import clothesline.service._
import clothesline.interop.nodetest._
import org.specs._
import clojure.lang._
import com.codahale.yoink.{PersistentHashMap => PMap}
import org.scala_tools.time.Imports._

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
        .annotate("y" -> "Another word")
      val pendingAnnMap = annotated.annotated()

      pendingAnnMap.count() must be_==( 2 )
      pendingAnnMap must havePair(keyword('x) -> "Word")
      pendingAnnMap must havePair(keyword('y) -> "Another word")
    }

    "accept headers" in {
      val headerizedRichResult = RichTestResult("hey") header( "X-Poop", "true" )
      val headers = headerizedRichResult.headers()

      headers.count() must be_==( 1 )
      headers must havePair("X-Poop", "true")
    }

    "accept headers and annotations together" in {
      val result = RichTestResult("D E V O").header("x" -> "y")
        .annotate("a" -> "b")
      val headers = result.headers()
      val annotated = result.annotated()

      headers.count() must be_==( 1 )
      headers must havePair("x" -> "y")
      annotated.count() must be_==( 1 )
      annotated must havePair(keyword('a) -> "b")
    }

    "allow JodaTime DateTime objects" in {
      val date = DateTime.now
      val richResult = RichTestResult(date)
      richResult.result must be_==(date)
    }

  }
}
