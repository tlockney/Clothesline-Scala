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

import clojure.lang.{Keyword, APersistentMap}
import com.codahale.yoink.{PersistentHashMap, PersistentTreeMap}

/**
 * General Clojure to Scala interop stuff
 */
object Util {
  val emptyMap = PersistentHashMap[Any,Any]()

  def keyword(str: String) = Keyword.intern(str)
  def keyword(sym: Symbol) = Keyword.intern(sym.name)

  implicit def string2Keyword(str: String): Keyword = keyword(str)
  implicit def symbol2Keyword(sym: Symbol): Keyword = keyword(sym.name)
  implicit def pairOfStringAndObject2PairOfKeywordAndObject(p: Pair[String, Object]): Pair[Keyword, Object] =
    Pair(keyword(p._1), p._2)

  implicit def keyword2String(key: Keyword): String = key.toString

  implicit def toAPersistentMap[A, B](pmap: PersistentTreeMap[A, B]): APersistentMap =
    pmap.underlying
  implicit def toAPersistentMap[A, B](pmap: PersistentHashMap[A, B]): APersistentMap =
    pmap.underlying
}
