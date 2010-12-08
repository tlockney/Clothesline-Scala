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

    val annotations = PMap()
    val res = PMap()

    val p = params(request)

    result(res) annotate(keyword('key) -> "value")
  }
  override def resourceExists(request: IPersistentMap, graphData: IPersistentMap) = {
    result(true)
  }
}

class CoreTests extends Specification {
  val eg = new ExampleService()
  val request = PMap() + (keyword('foo) -> "bar")
  val graphData = PMap()


  "Should be able to pimp paramters" in {
    import Parameters._

    var p: APersistentMap = PersistentHashMap.EMPTY
    p = p.assoc(Keyword.intern(":foo"),"bar").asInstanceOf[APersistentMap]
    p(keyword(":foo")) must be_==("bar")

  }

  "Ensure that we can create a base service" in {
    val testResult:TestResult = eg.contentTypesProvided(request.underlying, graphData.underlying)
    val result = testResult.result.asInstanceOf[APersistentMap]
    val annotations = testResult.annotations.asInstanceOf[APersistentMap]
    val richResult = RichTestResult(result, annotations)
  }
}
