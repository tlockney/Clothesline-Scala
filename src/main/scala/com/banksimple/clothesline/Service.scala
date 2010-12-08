package com.banksimple.clothesline

import clojure.lang.{AFn,Associative,IPersistentMap,APersistentMap,Keyword}
import clothesline.service.BaseService
import clothesline.interop.nodetest.TestResult
import com.codahale.yoink._

object Util {
  val emptyMap = PersistentHashMap[Any,Any]()

  def keyword(str: String) = Keyword.intern(str)
  def keyword(sym: Symbol) = Keyword.intern(sym.name)

  implicit def string2Keyword(str: String): Keyword = keyword(str)
  implicit def symbol2Keyword(sym: Symbol): Keyword = keyword(sym.name)
}

/**
 * Scala-fied "rich"-type version of TestResult
 */
class RichTestResult[T](result: T, ann: APersistentMap)  {
  val annotations = new PersistentHashMap[Keyword,PersistentHashMap[Keyword,Object]](ann)

  def asTestResult = new TestResult(result, annotations.underlying)

  val annotateKeyword = Keyword.intern("annotate")

  type SymOrKeyword = Symbol with Keyword

  def annotate(p: Tuple2[Keyword,Object]) = {
    val a = getAnnotateMap() + p
    val updatedAnnotations = annotations + (annotateKeyword -> a)
    new RichTestResult(result, updatedAnnotations.underlying)
  }

  def getAnnotateMap(): PersistentHashMap[Keyword,Object] = {
    annotations.getOrElse(annotateKeyword,
                          PersistentHashMap[Keyword,Object]())
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

  implicit def tuple2TestResult(t: Tuple2[IPersistentMap,IPersistentMap]): TestResult =
    new TestResult(t._1,t._2)
}

