# scalaAnnotationBug
Sample project showing a bug when reading annotations in a scala macro

run with
    **sbt bug/run**

This is related to [scala bug 7561](https://issues.scala-lang.org/browse/SI-7561)

Due to the bug, the macro [annotations](https://github.com/michael72/scalaAnnotationBug/blob/master/macros/src/main/scala/macros/TestMacros.scala) sometimes succeeds and sometimes fails in getting the annotations of members of a trait. 

The [Sample code](https://github.com/michael72/scalaAnnotationBug/blob/master/bug/src/main/scala/AnnotationBug.scala) shows some of this different behavior. 

The output of the program is:

    OK:   trait TestInnerDeclaredBefore - saved in current object: Some(List(anno))
    FAIL: trait TestInnerDeclaredBefore - saved to DeclaredBefore: None

    OK:   trait TestInVal declared in val: Some(List(anno))

    FAIL: trait TestInInner - saved to member in InnerObject: None
    OK:   trait TestInInner - saved in current object: Some(List(anno))

    OK:   trait TestLocalBefore in current object - declared before use - saved in current object: Some(List(anno))
    FAIL: trait TestLocalAfter in current object - declared after use - saved in current object: None

    FAIL: trait TestInnerDeclaredAfter - saved in current object: None
    FAIL: trait TestInnerDeclaredAfter - saved to DeclaredAfter: None

