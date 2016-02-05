package CensusModeling

/**
  * Created by brifkind on 28/01/2016.
  */
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}


class ReadInCSV(filePath:String = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\data\\",
               file:String = "Census.csv") {


    def readInCsvEasy(sqlContext:SQLContext): DataFrame = {
      sqlContext.read.format("com.databricks.spark.csv")
        .option("header", "true") // Use first line of all files as header
        .option("inferSchema", "true") // Automatically infer data types
        .load(filePath+file)
    }

}
