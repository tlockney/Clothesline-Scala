import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
  val yoink = "com.codahale" %% "yoink" % "1.1.1-SNAPSHOT" withSources()
}

