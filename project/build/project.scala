import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val bankSimpleRepoPath = System.getenv("BANKSIMPLE_DEV") + "/BankSimple-Maven-Repo"
  
  // repositories
  val bankSimpleSnapshotRepo = "BankSimple Snapshot Repository" at "file://" + bankSimpleRepoPath + "/snapshots"
  val clojureReleaseRepo = "Clojure Release Repository" at "http://build.clojure.org/releases"
  val clojureSnapshotRepo = "Clojure Snapshot Repository" at "http://build.clojure.org/snapshots"
  val clojarsRepo = "Clojars Repository" at "http://clojars.org/repo/"
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
  val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"

  // publishing
  val publishTo = Resolver.file("BankSimple Snapshot Repository", new java.io.File(bankSimpleRepoPath + "/snapshots"))

  // dependencies
  val clothesline = "Clothesline" % "Clothesline" % "0.1.0-SNAPSHOT"
  val yoink = "com.codahale" % "yoink_2.8.0.RC2" % "1.1.1-SNAPSHOT"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.5" % "test"
  val liftJson = "net.liftweb" %% "lift-json" % "2.1"
}

