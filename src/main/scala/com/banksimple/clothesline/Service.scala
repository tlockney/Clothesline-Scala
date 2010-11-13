package com.banksimple.clothesline

import clojure.lang.{AFn,Associative,IPersistentMap,Keyword}
import clothesline.service.BaseService
import clothesline.interop.nodetest.TestResult
import net.liftweb.json._
import net.liftweb.jso.JsonAST._
import net.liftweb.json.JsonParser._
import com.codahale.yoink._

/**
 * This will eventually be moved to the Clothesline-scala project
 */
trait ClotheslineService extends BaseService {
  implicit val formats = Serialization.formats(NoTypeHints)

  /**
   * Grab the params from the request
   */
  def params(request: IPersistentMap) =
    request.valAt(Keyword.intern("params")).asInstanceOf[IPersistentMap]

  /**
   * The return type needs to either be implied by context or specified
   * explicitly
   */
  def param[T](request: IPersistentMap, key: String): T =
    params(request).valAt(key).asInstanceOf[T]

  /**
   * Helper function for creating content type responders
   */
  def responder(f: => () => String) = new AFn() {
    override def invoke(a: Object, b: Object) = f()
  }

  /** Simple helper for getting an empty map instance */
  def emptyMap = PersistentHashMap().empty.underlying
}

object TransactionListService extends ClotheslineService {
  override def contentTypesProvided(req: IPersistentMap, graphData: IPersistentMap) = {
    val result = PersistentHashMap("application/json" -> responder { () =>
      Serialization.write(TransactionManager.getTransactions(""))
    })
    new TestResult(result.underlying, PersistentHashMap())
  }
}
