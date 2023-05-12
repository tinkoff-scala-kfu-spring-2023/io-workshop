lazy val root = (project in file("."))
  .settings(
    organization := "com.example",
    name := "io-workshop",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.4.11"
    ),
)
