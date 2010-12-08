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

  def annotate(p: Tuple2[Object,Object]) = {
    val a = getAnnotateMap() + p
    val updatedAnnotations = annotations + (annotateKeyword -> a)
    new RichTestResult(result, updatedAnnotations.underlying)
  }

  def getAnnotateMap(): PersistentHashMap[Object,Object] = {
    annotations.getOrElse(annotateKeyword,
                          PersistentHashMap[Object,Object]())
  }
}
object RichTestResult {
  def apply(b: Boolean) = new RichTestResult(b, Util.emptyMap.underlying)
  def apply(p: APersistentMap) = new RichTestResult(p, PersistentHashMap().underlying)
  def apply(r: APersistentMap, a: APersistentMap) = new RichTestResult(r, a)

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

