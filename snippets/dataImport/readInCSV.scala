// Read in a csv file as spark DataFrame. Specifically the census data set.

// use spark-csv to read in census so start shell with
// spark-shell --packages com.databricks:spark-csv_2.10:1.3.0

import org.apache.spark.sql.SQLContext

// who goes there?
val filePath = if(sys.env("LOGNAME") == "steve") {
  "/home/steve/sparkLearn/data/Census.csv";
} else {
  "/home/ben/Documents/phoenix/sparkLearn/data/Census.csv";
}

val census = sqlContext.read.format("com.databricks.spark.csv")  
            .option("header", "true") // Use first line of all files as header
            .option("inferSchema", "true") // Automatically infer data types 
            .load(filePath)
