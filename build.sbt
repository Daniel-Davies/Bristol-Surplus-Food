name := """efficace-server"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.3"

libraryDependencies += guice

// DB
libraryDependencies ++= Seq(evolutions)
libraryDependencies += "tyrex" % "tyrex" % "1.0.1"
libraryDependencies += javaJdbc
libraryDependencies += javaJpa
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.2.5.Final"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1212"

libraryDependencies += "org.mockito" % "mockito-core" % "2.10.0" % "test"
// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.194"

// Auth
val pac4jVersion = "2.2.1"

libraryDependencies += "org.pac4j" %% "play-pac4j" % "5.0.0"
libraryDependencies += "org.pac4j" % "pac4j-http" % pac4jVersion
libraryDependencies += "org.pac4j" % "pac4j-sql" % pac4jVersion
libraryDependencies += "org.pac4j" % "pac4j-jwt" % pac4jVersion
libraryDependencies += ehcache
libraryDependencies += "org.springframework.security" % "spring-security-core" % "3.1.0.RELEASE"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

PlayKeys.externalizeResources := false

