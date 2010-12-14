package com.banksimple.clothesline

import clojure.lang.{AFn,Associative,IPersistentMap,APersistentMap,Keyword}
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

/**
 * Customized Scala version of BaseService
 */
class Service extends BaseService {
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

