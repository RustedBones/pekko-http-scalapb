// General info
val username  = "RustedBones"
val repo      = "pekko-http-scalapb"
val githubUrl = s"https://github.com/$username/$repo"

ThisBuild / tlBaseVersion := "1.0"
ThisBuild / organization := "fr.davit"
ThisBuild / organizationName := "Michel Davit"
ThisBuild / startYear := Some(2019)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / homepage := Some(url(githubUrl))
ThisBuild / scmInfo := Some(ScmInfo(url(githubUrl), s"git@github.com:$username/$repo.git"))
ThisBuild / developers := List(
  Developer(
    id = s"$username",
    name = "Michel Davit",
    email = "michel@davit.fr",
    url = url(s"https://github.com/$username")
  )
)

// scala versions
val scala3       = "3.3.3"
val scala213     = "2.13.13"
val defaultScala = scala3

// github actions
val java17      = JavaSpec.temurin("17")
val java11      = JavaSpec.temurin("11")
val defaultJava = java17

ThisBuild / scalaVersion := defaultScala
ThisBuild / crossScalaVersions := Seq(scala3, scala213)
ThisBuild / githubWorkflowTargetBranches := Seq("main")
ThisBuild / githubWorkflowJavaVersions := Seq(java17, java11)

// build
ThisBuild / tlFatalWarnings := true
ThisBuild / tlJdkRelease := Some(8)
ThisBuild / tlSonatypeUseLegacyHost := true

// mima
ThisBuild / tlBaseVersion := "1.0"
ThisBuild / mimaBinaryIssueFilters ++= Seq()

// scalaPB
val protoSourceManaged = settingKey[File]("Default directory for generated protobuf by the build.")
ThisBuild / protoSourceManaged := sourceManaged.value / "compiled_proto"
ThisBuild / PB.protocVersion := Dependencies.Versions.Protobuf

lazy val protobufConfigSettings = Def.settings(
  PB.targets := Seq(
    scalapb.gen(grpc = false) -> Defaults.configSrcSub(protoSourceManaged).value
  ),
  managedSourceDirectories ++= PB.targets.value.map(_.outputPath)
)

lazy val protobufSettings = Seq(Compile, Test).flatMap(c => inConfig(c)(protobufConfigSettings)) ++
  inConfig(Test)(
    Def.settings(
      // generated protobuf discards non-unit value
      scalacOptions ~= { _.filterNot(_ == "-Wvalue-discard") }
    )
  )

lazy val `pekko-http-scalapb` = (project in file("."))
  .dependsOn(`pekko-http-scalapb-binary`, `pekko-http-scalapb-json4s`)
  .aggregate(`pekko-http-scalapb-binary`, `pekko-http-scalapb-json4s`)
  .settings(protobufSettings)
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.PekkoHttp,
      Dependencies.ScalaPB,
      Dependencies.Provided.PekkoStream,
      Dependencies.Test.PekkoHttpTestkit,
      Dependencies.Test.PekkoTestkit,
      Dependencies.Test.ScalaTest
    )
  )

lazy val `pekko-http-scalapb-binary` = (project in file("binary"))
  .settings(protobufSettings)
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.PekkoHttp,
      Dependencies.ScalaCollectionCompat,
      Dependencies.ScalaPB,
      Dependencies.Provided.PekkoStream,
      Dependencies.Test.PekkoHttpTestkit,
      Dependencies.Test.PekkoTestkit,
      Dependencies.Test.ScalaTest
    )
  )

lazy val `pekko-http-scalapb-json4s` = (project in file("json4s"))
  .settings(protobufSettings)
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.PekkoHttp,
      Dependencies.ScalaCollectionCompat,
      Dependencies.ScalaPB,
      Dependencies.ScalaPbJson4s,
      Dependencies.Provided.PekkoStream,
      Dependencies.Test.PekkoHttpTestkit,
      Dependencies.Test.PekkoTestkit,
      Dependencies.Test.ScalaTest
    )
  )
