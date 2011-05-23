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
