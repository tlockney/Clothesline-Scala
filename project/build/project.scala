import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with IdeaProject {

  // repositories
  val clojars = "clojars.org" at "http://clojars.org/repo"
  val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"

  // dependencies
  val clothesline = "clothesline" % "clothesline" % "0.2.0-SNAPSHOT"
  val yoink = "com.codahale" % "yoink_2.8.0.RC2" % "1.1.1-SNAPSHOT"
  val logula = "com.codahale" %% "logula" % "2.1.2"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test"
  val scalaTime = "org.scala-tools.time" % "time_2.8.0" % "0.2"
}

