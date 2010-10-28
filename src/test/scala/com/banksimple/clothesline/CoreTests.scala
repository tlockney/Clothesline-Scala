package com.banksimple.clothesline

import clothesline._
import clothesline.service._
import clothesline.interop.nodetest._

import org.specs._

import clojure.lang._

import com.codahale.yoink.{PersistentHashMap => PMap}

class ExampleService extends BaseService {
  override def contentTypesProvided(request: clojure.lang.IPersistentMap, graphData: clojure.lang.IPersistentMap): TestResult = {
    var testResult = new TestResult(PersistentHashMap.EMPTY, PersistentHashMap.EMPTY)
    var annotations = testResult.annotations.asInstanceOf[IPersistentMap]
    var result = testResult.result.asInstanceOf[IPersistentMap] 
    testResult
  }
}

class CoreTests extends Specification {
  "Ensure that we can create a base service" in {
    val eg = new ExampleService() 
    val request = PMap()
    val graphData = PMap()
    eg.contentTypesProvided(request.underlying, graphData.underlying)
  }
}
