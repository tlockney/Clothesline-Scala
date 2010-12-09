package com.banksimple.clothesline

import clojure.lang.{Keyword,APersistentMap}
import com.codahale.yoink.{PersistentHashMap,PersistentTreeMap}

/**
 * General Clojure to Scala interop stuff
 */
object Util {
  val emptyMap = PersistentHashMap[Any,Any]()

  def keyword(str: String) = Keyword.intern(str)
  def keyword(sym: Symbol) = Keyword.intern(sym.name)

  implicit def string2Keyword(str: String): Keyword = keyword(str)
  implicit def symbol2Keyword(sym: Symbol): Keyword = keyword(sym.name)
  implicit def spair2KPair(p: Pair[String,Object]): Pair[Keyword,Object] = Pair(keyword(p._1),p._2)

  implicit def keyword2String(key: Keyword): String = key.toString

  implicit def toAPersistentMap[A,B](pmap: PersistentTreeMap[A,B]): APersistentMap = pmap.underlying
  implicit def toAPersistentMap[A,B](pmap: PersistentHashMap[A,B]): APersistentMap = pmap.underlying
}
