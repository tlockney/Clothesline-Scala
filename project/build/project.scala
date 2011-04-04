import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with IdeaProject {

  val publishTo = "BankSimple Nexus Snapshots" at "http://nexus.banksimple.com/content/repositories/snapshots"

  Credentials(Path.userHome / ".ivy2" / ".credentials", log)

  override def repositories = Set("BankSimple Nexus Repo" at "http://nexus.banksimple.com/content/groups/public",
                                  "Local M2 Repo" at "file://" + (Path.userHome/".m2"/"repository").toString)

  override def ivyRepositories = Seq(Resolver.defaultLocal(None)) ++ repositories

  // dependencies
  val clothesline = "clothesline" % "clothesline" % "0.1.1-SNAPSHOT"
  val yoink = "com.codahale" % "yoink_2.8.0.RC2" % "1.1.1-SNAPSHOT"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test"
  val scalaTime = "org.scala-tools.time" % "time_2.8.0" % "0.2"
  val bsUtil = "com.banksimple" %% "util" % "0.1.0-SNAPSHOT"
}

