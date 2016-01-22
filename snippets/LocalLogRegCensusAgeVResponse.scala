// use spark-csv to read in census so start shell with
// spark-shell --packages com.databricks:spark-csv_2.10:1.3.0

import org.apache.spark.sql.SQLContext
import sqlContext.implicits._ // for implicit conversions. eg. RDD to dataframe

val census = sqlContext.read.format("com.databricks.spark.csv")  
            .option("header", "true") // Use first line of all files as header
            .option("inferSchema", "true") // Automatically infer data types 
            .load("/home/ben/Documents/phoenix/sparkLearn/data/Census.csv")     

// model just age against response
val ageVsResponse = census.select("age", "Response")

//transform Response (yes/no) to (1/0). Don't yet care how to specify which category is the target.
import org.apache.spark.ml.feature.StringIndexer
val indexer = new StringIndexer()
  .setInputCol("Response")
  .setOutputCol("BinaryResponse")
  .fit(ageVsResponse)
val ageVRBinary  = indexer.transform(ageVsResponse).select("age", "BinaryResponse")



// data frame must be in the form of Labeled Point which is (label, feature vector)
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint

// to RDD of (label, feature vector), then to DF
// Do I really need to do this?? If working with data frames shouldn't have to convert back and forth with RDDs
val age = ageVRBinary.map(row => LabeledPoint(row.getDouble(1), Vectors.dense(row.getInt(0)))).toDF("response", ageVRBinary.columns(0))


import org.apache.spark.ml.classification.LogisticRegression
// setting column names for regression. defaults are: (label, features) for (dependent, independent). These are also the defaults when we mapped RDD of LabeledPoints to dataframe.  
val lr = new LogisticRegression().setLabelCol("response").setFeaturesCol("age")
val model = lr.fit(age)

// model.coefficients // check out the coefficients
// model.intercept //intercept
// model.transform(age.select("age")).show //make a prediction on the data