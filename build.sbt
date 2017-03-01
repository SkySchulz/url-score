name := "url-score"
organization := "com.skyschulz"

version := "0.2.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala,BuildInfoPlugin,ElasticBeanstalkPlugin)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  cache,
  filters,
  // TODO migrate to iheart swagger integration https://github.com/iheartradio/play-swagger
  // the iheart play swagger performs all swagger specification in the routes file, keeping the code annotation-free
  "io.swagger" %% "swagger-play2" % "1.5.3", // https://github.com/swagger-api/swagger-play/tree/master/play-2.5/swagger-play2
  "org.webjars" % "swagger-ui" % "2.2.10",  // https://github.com/swagger-api/swagger-ui  https://mvnrepository.com/artifact/org.webjars/swagger-ui
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

// Adds additional packages into Twirl
TwirlKeys.templateImports += "build._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.skyschulz.binders._"

// Docker/Elastic Beanstalk
// To generate a docker container for local evaluation:
//   $ sbt docker:publishLocal
//   $ docker run -p 9000:9000 <docker-container-id-reported-above>
// To generate a zip file ready for deployment by AWS Elastic Beanstalk:
//   $ sbt elastic-beanstalk:dist
maintainer in Docker := "Sky Schulz <skygit@ogn.org>"
dockerExposedPorts := Seq(9000)
dockerBaseImage := "openjdk:8-jre"
dockerRepository := Some("skyschulz")

// BuildInfoPlugin
buildInfoPackage := "build"
