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
import com.codahale.logula.Logging
import Util._

/**
 * Customized Scala version of BaseService
 */
class Service extends BaseService with Logging {
  import Util._
  import org.scala_tools.time.Imports._

  def result(r: Boolean) = RichTestResult(r)
  def result(p: APersistentMap) = RichTestResult(p)
  def result[K, V](p: PersistentHashMap[K, V]) = RichTestResult(p.underlying)
  def result(p: clojure.lang.PersistentHashSet) = RichTestResult(p)
  def result(d: DateTime) = RichTestResult(d)

  /**
   * Returns a Parameter object which is really a PersistentHashMap
   */
  def params(request: IPersistentMap): Parameters = Parameters.fromRequest(request)

  /**
   * Gets the body of the request
   */
  def body(request: IPersistentMap): java.io.InputStream =
    request.valAt(keyword("body")).asInstanceOf[java.io.InputStream]

  val debugLogger = new AFn() with Fn {
    override def invoke(msg: Object): Object = {
      log.debug("%s",msg)
      new Object()
    }
  }

  /**
   * Helper function for creating content type responders
   */
  def responder(f: => String) = new AFn() {
    override def invoke(a: Object, b: Object) = f
  }

  def responders(resp: Tuple2[String, AFn]*): PersistentHashMap[String, AFn] = {
    var map = PersistentHashMap[String, AFn]()
    resp.toMap[String, AFn].foreach { r => map = map + r }
    map
  }

  implicit def responders2TestResult(r: PersistentHashMap[String, AFn]): TestResult =
    new TestResult(r.underlying, Util.emptyMap.underlying)

  implicit def tuple2TestResult(t: Tuple2[IPersistentMap, IPersistentMap]): TestResult =
    new TestResult(t._1, t._2)
}

