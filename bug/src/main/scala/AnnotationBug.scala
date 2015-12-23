import macros.TestMacros
import scala.annotation.StaticAnnotation
import scala.annotation.meta._


// small example annotation
@getter @setter
class anno extends StaticAnnotation

// Trait in object - declared before use
object DeclaredBefore {
  trait TestInnerDeclaredBefore {
    @anno
    var y = 1
  }
  val a = TestMacros.annotations[TestInnerDeclaredBefore] // saved to same object after declaration: fails!
}

object AnnotationBug {
  def main(args: Array[String]): Unit = {
    val y = TestMacros.annotations[DeclaredBefore.TestInnerDeclaredBefore]
    val z = {
      trait TestInVal {
        @anno
        var z = 2
      }
      TestMacros.annotations[TestInVal]
    }
    
    object InnerObject {
      trait TestInInner {
        @anno
        var u = 3
      }
      val u = TestMacros.annotations[TestInInner]
    }
    val u2 = TestMacros.annotations[InnerObject.TestInInner]
    
   
    trait TestLocalBefore {
      @anno
      var v = 4
    }
    val v1 = TestMacros.annotations[TestLocalBefore]
    val v2 = TestMacros.annotations[TestLocalAfter]
    val w = TestMacros.annotations[DeclaredAfter.TestInnerDeclaredAfter]
    
    var failed = false
    def printResult(msg: String, res: Option[List[String]]) {
       println(s"""${if(res == None) "FAIL:" else "OK:  "} ${msg}: ${res.toString}""")
       if (res == None) failed = true
    }
    println("")
    println("#########################################");
    printResult("trait TestInnerDeclaredBefore - saved in current object", y.get("y"))
    printResult("trait TestInnerDeclaredBefore - saved to DeclaredBefore", DeclaredBefore.a.get("y"))
    println("")
    printResult("trait TestInVal declared in val", z.get("z"))
    println("")
    printResult("trait TestInInner - saved to member in InnerObject", InnerObject.u.get("u"))
    printResult("trait TestInInner - saved in current object", u2.get("u"))
    println("")
    printResult("trait TestLocalBefore in current object - declared before use - saved in current object", v1.get("v"))    
    printResult("trait TestLocalAfter in current object - declared after use - saved in current object", v2.get("v"))    
    println("")
    printResult("trait TestInnerDeclaredAfter - saved in current object", w.get("w"))
    printResult("trait TestInnerDeclaredAfter - saved to DeclaredAfter", DeclaredAfter.a.get("w"))
    println("#########################################");
    if (failed) {
       println("TEST FAILED: got None result(s) :(")
       System.exit(-1)
    } else println("TEST SUCCEEDED :)")
    println("#########################################");
    
    trait TestLocalAfter {
      @anno
      var v = 4
    }
  }  
}

// Trait in object - declared after use
object DeclaredAfter {
  trait TestInnerDeclaredAfter {
    @anno
    var w = 5
  }
  val a = TestMacros.annotations[TestInnerDeclaredAfter]
}
