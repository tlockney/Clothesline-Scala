/*
 * Copyright 2011 Simple Finance, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.banksimple.clothesline

import clojure.lang.{AFn,Fn,Associative,IPersistentMap,APersistentMap,Keyword}
import clothesline.service.BaseService
import clothesline.interop.nodetest.TestResult
import com.codahale.yoink._
import Util._

/**
 * Scala-fied "rich"-type version of TestResult
 */
class RichTestResult[T](result: T, var annotations: APersistentMap) {

  private def asTestResult = new TestResult(result, annotations)

  private val annotateKeyword = Keyword.intern("annotate")
  private val headersKeyword = Keyword.intern("headers")

  def annotate(p: Tuple2[Keyword, Object]) = {
    updateSubMap(annotateKeyword, p)
  }

  def header(p: Tuple2[String, String]) =
    updateSubMap(headersKeyword, p)

  def annotated() = new PersistentHashMap[Keyword,Object](getSubMap(annotateKeyword))
  def headers() = new PersistentHashMap[String, String](getSubMap(headersKeyword))


  private def updateSubMap(k: Keyword, p: Tuple2[Object, Object]) = {
    val map: APersistentMap = getSubMap(k)
    val updatedMap = map.assoc(p._1, p._2)
    val updatedAnnotations = annotations.assoc(k, updatedMap).asInstanceOf[APersistentMap]
    new RichTestResult(result, updatedAnnotations)
  }

  private def getSubMap(k: Keyword): APersistentMap = {
    val subMap = if (annotations.containsKey(k)) { annotations.get(k).asInstanceOf[APersistentMap] }
      else { clojure.lang.PersistentHashMap.EMPTY }
    annotations = annotations.assoc(k, subMap).asInstanceOf[APersistentMap]
    subMap
  }

}

object RichTestResult {
  def apply(r: APersistentMap, a: APersistentMap) = new RichTestResult(r, a)
  def apply[T](o: T) = new RichTestResult(o, Util.emptyMap.underlying)

  implicit def richTestResult2TestResult[T](p: RichTestResult[T]): TestResult =
    p.asTestResult
}
