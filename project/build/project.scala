import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
  val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
  val yoink = "com.codahale" %% "yoink" % "1.1.1-SNAPSHOT" withSources()
  val clothesline = "Clothesline" % "Clothesline" % "1.0.0-SNAPSHOT"
}

