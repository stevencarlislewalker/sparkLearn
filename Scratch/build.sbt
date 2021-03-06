name := "Scratch"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.6.0"
libraryDependencies += "org.apache.spark" % "spark-mllib_2.10" % "1.6.0"
libraryDependencies +=  "com.databricks" % "spark-csv_2.10" % "1.3.0"
libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"