import sbt._
import Keys._

object BuildSettings {
  val paradiseVersion = "2.0.0"
  val usedScalaVersion = "2.11.7" // "2.10.4", // "2.12.0-M3" // 
  
  val addSettings = if (usedScalaVersion.startsWith("2.10")) Seq(crossScalaVersions := Seq("2.10.2", "2.10.3", "2.10.4", "2.11.0"),
    addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full))
  else Nil
    
  val buildSettings = Defaults.defaultSettings ++ Seq (
    version       := "1.0.0",
    scalaVersion  :=  usedScalaVersion
    ) ++ addSettings
}

object ConfigMacroBuild extends Build {
  import BuildSettings._
  
  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings 
  ) aggregate(macros, bug) dependsOn(macros, bug)

  
  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
      libraryDependencies ++= (
        if (scalaVersion.value.startsWith("2.10")) List("org.scalamacros" %% "quasiquotes" % paradiseVersion)
        else Nil
      ))  
  )
  
  
  lazy val bug: Project = Project(
    "bug",
    file("bug"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)) 
  ) dependsOn(macros)
  
  
 
}
