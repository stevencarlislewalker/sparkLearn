How to load a csv file into a data frame using [spark-csv](https://github.com/databricks/spark-csv) on a local instance of Spark. This is running on Ubuntu with no hfds set up.  

Run spark-shell with spark-csv package  
`spark-shell --packages com.databricks:spark-csv_2.10:1.3.0`  
Hopefully spark-shell is in your path. This is the package for scala 2.10 (different than the scala I installed but the same as the scala that comes with the Spark distribution). 

Inside of Spark just run the following scala code. The command `:paste` is useful which lets you paste in the whole section of code. Otherwise the scala REPL thinks you're done at the end of a line and throws an error.
````scala   
import org.apache.spark.sql.SQLContext
val sqlContext = new SQLContext(sc)  
val df = sqlContext.read.format("com.databricks.spark.csv")  
			.option("header", "true") // Use first line of all files as header
			.option("inferSchema", "true") // Automatically infer data types 
			.load("filePathToCsv")     
```` 
Just change filePathToCsv to your filepath to csv. There is no need to prepend `file:\\`. Not sure why. Maybe because there's no hdfs set up as default so Spark knows to point to the local file system. 
