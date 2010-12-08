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

class CoreTests extends Specification {
  val eg = new ExampleService()
  val request = PMap() + (keyword('foo) -> "bar")
  val graphData = PMap()

  "Should be able to pimp parameters" in {
    import Parameters._

    var p: APersistentMap = PersistentHashMap.EMPTY
    p = p.assoc(keyword(":foo"),"bar").asInstanceOf[APersistentMap]
    p(keyword(":foo")) must be_==("bar")
  }

  "Ensure that we can create a base service" in {
    val testResult = eg.contentTypesProvided(request.underlying, graphData.underlying)
    val result = testResult.result.asInstanceOf[APersistentMap]
    val annotations = testResult.annotations.asInstanceOf[APersistentMap]
  }

  "Assure responders are getting set correctly" in {
    val testResult = eg.contentTypesProvided(request.underlying, graphData.underlying)
    val resultMap = testResult.result.asInstanceOf[APersistentMap]
    val results = new PMap[String,AFn](resultMap)
    results("text/plain").invoke(null,null) must be_==("this is a test")
  }


}

class RichResultTests extends Specification {

  "RichTestResult" should {
    "accept annotations." in  {

      val richResult = RichTestResult(true)
      val annotated  = richResult.annotate("x" -> "Word")
      val annMap = annotated.annotations
      val pendingAnnMap = annMap(keyword("annotate"))

      pendingAnnMap.count() must be_==( 1 )
      pendingAnnMap must havePair(keyword('x) -> "Word")
    }

    "accept headers." in {
      import Util._
      val headerizedRichResult = RichTestResult("hey") header( "X-Poop", "true" )
      val annMap = headerizedRichResult.annotations

      annMap must haveKey(keyword("headers"))
      annMap(keyword("headers")) must havePair("X-Poop", "true")
    }
  }
}
