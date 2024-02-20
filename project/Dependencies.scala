import sbt._

object Dependencies {

  object Versions {
    val Pekko                 = "1.0.2"
    val PekkoHttp             = "1.0.0"
    val Protobuf              = "3.24.4"
    val ScalaCollectionCompat = "2.11.0"
    val ScalaPB               = "0.11.14"
    val ScalaPBJson4s         = "0.12.1"
    val ScalaTest             = "3.2.18"
  }

  val PekkoHttp             = "org.apache.pekko"       %% "pekko-http"              % Versions.PekkoHttp
  val ScalaCollectionCompat = "org.scala-lang.modules" %% "scala-collection-compat" % Versions.ScalaCollectionCompat
  val ScalaPB               = "com.thesamet.scalapb"   %% "scalapb-runtime"         % Versions.ScalaPB
  val ScalaPbJson4s         = "com.thesamet.scalapb"   %% "scalapb-json4s"          % Versions.ScalaPBJson4s

  object Provided {
    val PekkoStream = "org.apache.pekko" %% "pekko-stream" % Versions.Pekko % "provided"
  }

  object Test {
    val PekkoHttpTestkit = "org.apache.pekko" %% "pekko-http-testkit" % Versions.PekkoHttp % "test"
    val PekkoTestkit     = "org.apache.pekko" %% "pekko-testkit"      % Versions.Pekko     % "test"
    val ScalaTest        = "org.scalatest"    %% "scalatest"          % Versions.ScalaTest % "test"
  }
}
