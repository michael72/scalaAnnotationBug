package macros

import scala.language.experimental.macros // 2.10
//import scala.reflect.macros.whitebox.Context // 2.11, 2.12

import scala.reflect.macros.Context
import scala.collection.immutable.Map
import scala.collection.immutable.List


object TestMacros {

  def annotations[A](): Map[String, List[String]] = macro annotationsImpl[A]
  
  def annotationsImpl[A: c.WeakTypeTag](c: Context)() = {
    import c.universe._
    val annotations = weakTypeOf[A].members.filter { m => m.annotations.size > 0 && m.isPublic }.map {
      m => m.name.toString -> m.annotations.toList.map(_.toString)
    }
    c.Expr(q"Map(..$annotations)")
  }
}