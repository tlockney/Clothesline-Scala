import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val bankSimpleRepoPath = System.getenv("BANKSIMPLE_DEV") + "/BankSimple-Maven-Repo/snapshots"

  val publishTo = Resolver.ssh(bankSimpleRepoPath, "localhost")
  // repositories
  val bankSimpleSnapshotRepo = "BankSimple Snapshot Repository" at "file://" + bankSimpleRepoPath
  val clojureReleaseRepo = "Clojure Release Repository" at "http://build.clojure.org/releases"
  val clojureSnapshotRepo = "Clojure Snapshot Repository" at "http://build.clojure.org/snapshots"
  val clojarsRepo = "Clojars Repository" at "http://clojars.org/repo/"
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
  val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"

  // dependencies
  val clothesline = "clothesline" % "clothesline" % "0.1.1-SNAPSHOT"
  val yoink = "com.codahale" % "yoink_2.8.0.RC2" % "1.1.1-SNAPSHOT"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test"
  val scalaTime = "org.scala-tools.time" % "time_2.8.0" % "0.2"
}

