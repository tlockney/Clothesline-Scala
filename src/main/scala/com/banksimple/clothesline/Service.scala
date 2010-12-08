package com.banksimple.clothesline

import clojure.lang.{AFn,Associative,IPersistentMap,APersistentMap,Keyword}
import clothesline.service.BaseService
import clothesline.interop.nodetest.TestResult
import com.codahale.yoink._
import Util._

/**
 * Scala-fied "rich"-type version of TestResult
 */
class RichTestResult[T](result: T, ann: APersistentMap)  {
  val annotations = new PersistentHashMap[Keyword,PersistentHashMap[Object,Object]](ann)

  def asTestResult = new TestResult(result, annotations.underlying)

  val annotateKeyword = Keyword.intern("annotate")
  val headersKeyword = Keyword.intern("headers")

  def annotate(p: Tuple2[Keyword,Object]) =
    updateSubMap(annotateKeyword, p)

  def header(p: Tuple2[String,String]) =
    updateSubMap(headersKeyword, p)

  def updateSubMap(k: Keyword, p: Tuple2[Object,Object]) = {
    val map = getSubMap(k) + p
    val updatedAnnotations = annotations + (k -> map)
    new RichTestResult(result, updatedAnnotations.underlying)
  }

  def getSubMap(k: Keyword): PersistentHashMap[Object,Object] =
    annotations.getOrElse(k, PersistentHashMap[Object,Object]())

}
object RichTestResult {
  //def apply(b: Boolean) = new RichTestResult(b, Util.emptyMap.underlying)
  //def apply(p: APersistentMap) = new RichTestResult(p, Util.emptyMap.underlying)
  def apply(r: APersistentMap, a: APersistentMap) = new RichTestResult(r, a)
  def apply[T](o: T) = new RichTestResult(o, Util.emptyMap.underlying)

  implicit def richTestResult2TestResult[T](p: RichTestResult[T]): TestResult =
    p.asTestResult
}

/**
 * Scala-fied Parameters object
 */
class Parameters(paramMap: APersistentMap) extends PersistentHashMap[Keyword,String](paramMap)
object Parameters {
  def apply(params: APersistentMap) = new Parameters(params)
  implicit def cljMap2Paramters(p: APersistentMap): Parameters = Parameters(p)
}

/**
 * Customized Scala version of BaseService
 */
class Service extends BaseService {
  import Util._

  def result(r: Boolean) = RichTestResult(r)
  def result(p: APersistentMap) = RichTestResult(p)
  def result[K,V](p: PersistentHashMap[K,V]) = RichTestResult(p.underlying)
  def result[T](p: T) = RichTestResult(p)

  /**
   * Returns a Parameter object which is really a PersistentHashMap
   */
  def params(request: IPersistentMap): Parameters = {
    val ps = request.valAt(keyword("params")).asInstanceOf[APersistentMap]
    Parameters(ps)
  }

  /**
   * Helper function for creating content type responders
   */
  def responder(f: => String) = new AFn() {
    override def invoke(a: Object, b: Object) = f
  }

  def responders(resp: Tuple2[String, AFn]*): PersistentHashMap[String,AFn] = {
    var map = PersistentHashMap[String,AFn]()
    resp.toMap[String,AFn].foreach { r => map = map + r }
    map
  }

  implicit def responders2TestResult(r: PersistentHashMap[String,AFn]): TestResult =
    new TestResult(r.underlying, Util.emptyMap.underlying)

  implicit def tuple2TestResult(t: Tuple2[IPersistentMap,IPersistentMap]): TestResult =
    new TestResult(t._1, t._2)
}

