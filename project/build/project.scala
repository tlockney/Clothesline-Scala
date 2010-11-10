import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  // repositories
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
  val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
  val clojureRepo = "Clojure Repository" at "http://build.clojure.org/releases"

  // dependencies
  val yoink = "com.codahale" % "yoink_2.8.0.RC2" % "1.1.1-SNAPSHOT"
  val clothesline = "Clothesline" % "Clothesline" % "1.0.0-SNAPSHOT"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.5" % "test"
  val clojure = "org.clojure" % "clojure" % "1.2.0"
  val clojureContrib = "org.clojure" % "clojure-contrib" % "1.2.0"
  val jettyServer = "org.mortbay.jetty" % "jetty" % "6.1.14"
  val commonsCodec = "commons-codec" % "commons-codec" % "1.4"
}

