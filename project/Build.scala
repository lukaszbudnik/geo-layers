import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "geo-layers"
  val appVersion = "0.1"

  val appDependencies = Seq(
    "se.radley" %% "play-plugins-salat" % "1.2",
    "com.novus" %% "salat" % "1.9.2-SNAPSHOT")

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    routesImport += "se.radley.plugin.salat.Binders._",
    templatesImport += "org.bson.types.ObjectId",
    offline := true)

}
