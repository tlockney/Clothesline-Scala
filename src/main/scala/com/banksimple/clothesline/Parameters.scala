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
 * Scala-fied Parameters object
 */
class Parameters(paramMap: APersistentMap) extends PersistentHashMap[String, String](paramMap)
object Parameters {
  def apply(params: APersistentMap) = new Parameters(params)
  def fromRequest(request: IPersistentMap): Parameters =
    new Parameters(request.valAt(keyword("params")).asInstanceOf[APersistentMap])
  implicit def cljMap2Paramters(p: APersistentMap): Parameters = Parameters(p)
}
