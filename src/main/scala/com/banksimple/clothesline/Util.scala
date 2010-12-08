package com.banksimple.clothesline

import clojure.lang.Keyword
import com.codahale.yoink._

/**
 * General Clojure to Scala interop stuff
 */
object Util {
  val emptyMap = PersistentHashMap[Any,Any]()

  def keyword(str: String) = Keyword.intern(str)
  def keyword(sym: Symbol) = Keyword.intern(sym.name)

  implicit def string2Keyword(str: String): Keyword = keyword(str)
  implicit def symbol2Keyword(sym: Symbol): Keyword = keyword(sym.name)
}
