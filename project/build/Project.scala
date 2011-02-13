import sbt._

class Project (info: ProjectInfo) extends DefaultWebProject(info) {

  val jettyVersion = "6.1.22"
  val servletVersion = "2.5"
  val slf4jVersion = "1.6.0"
  val scalatraVersion = "2.0.0-SNAPSHOT"
  val scalateVersion = "1.2"
  val scalaTestVersion = "1.2-for-scala-2.8.0.final-SNAPSHOT"
  val LiftWebVersion = "2.1"

  val liftJson = "net.liftweb" % "lift-json_2.8.0" % LiftWebVersion
  val jetty6 = "org.mortbay.jetty" % "jetty" % jettyVersion % "test"
  val servletApi = "javax.servlet" % "servlet-api" % servletVersion % "provided"
  val jettyClient = "org.mortbay.jetty" % "jetty-client" % "6.1.25" withSources()
  val httpClient = "commons-httpclient" % "commons-httpclient" % "3.0.1" withSources()
  val casbah = "com.mongodb.casbah" % "casbah_2.8.1" % "2.0.2"
  // scalaTest
  val scalaTest = "org.scalatest" % "scalatest" % scalaTestVersion % "test"

  // scalatra
  val scalatra = "org.scalatra" %% "scalatra" % scalatraVersion

  // scalate
  val scalate = "org.fusesource.scalate" % "scalate-core" % scalateVersion
  val scalatraScalate = "org.scalatra" %% "scalatra-scalate" % scalatraVersion

  val sfl4japi = "org.slf4j" % "slf4j-api" % slf4jVersion % "runtime"
  val sfl4jnop = "org.slf4j" % "slf4j-nop" % slf4jVersion % "runtime"

  // repositories
  val scalaToolsSnapshots = "Scala Tools Repository" at "http://nexus.scala-tools.org/content/repositories/snapshots/"
  val sonatypeNexusSnapshots = "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  val sonatypeNexusReleases = "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases"
  val fuseSourceSnapshots = "FuseSource Snapshot Repository" at "http://repo.fusesource.com/nexus/content/repositories/snapshots"
  //val guardianNexus = "Guardian Nexus" at "http://nexus.gudev.gnl:8081/nexus/content/groups/public/"
  val guardianGithub = "Guardian Github Releases" at "http://guardian.github.com/maven/repo-releases"
  val scalaTools = "scala-tools releases" at "http://scala-tools.org/repo-releases/"

  val ossSnapsohts = "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

  val buzzMediaRepo = "The Buzz Media Maven Repository" at "http://maven.thebuzzmedia.com"

}