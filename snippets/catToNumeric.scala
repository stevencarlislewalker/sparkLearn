import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.feature.{StringIndexer, IndexToString}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.sql.types._


// This script is meant to get a data frame into shape so that it can be fed into an mllib model. That means that all categorical data (strings) are encoded as doubles - Eg., ("Yes"/ "No") ==> (0/1). And that it ends up as a data frame of LabeledPoints.



// helper functions

// index a categorical column from a data frame and append to the end of the data frame
// return a data frame
def indexCategorical(arg: String, df:DataFrame) : DataFrame = {
      val indexer = new StringIndexer()
      indexer.fit(df).transform(df)
}


// takes a row, and integer index of column entries and returns a list of doubles corresponding to those entries
def toVector(row:Row, index:List[Int]):Vector = {
    val schema = row.schema

    // extract features as doubles
    val features = index.map(x=> schema(x).dataType match {
    case IntegerType => row.getInt(x).toDouble
    case DoubleType => row.getDouble(x)
    })
    Vectors.dense(features.head, features.tail:_*)
}

// end helper functions

// the starting data frame (we use census)
val census = sqlContext.read.format("com.databricks.spark.csv")
            .option("header", "true") // Use first line of all files as header
            .option("inferSchema", "true") // Automatically infer data types
            .load("/Users/Ben/Documents/phoenix/sparkLearn/data/Census.csv")

// names of columns we want to select for
val cats = List("workclass", "education", "marital-status", "sex") //categorical
val num = List("age") // numeric (continuous)
val label = "Response" // dependent variable


// convert all categories (including label) to numeric
val census_cats = (label :: cats).foldLeft(census)((df, arg) => catToNumber(arg, df))

// find indices of variables
val columns = census_cats.columns.toList.zipWithIndex //column names with index

// find integer indices variables

// integer indices of independent variables
val cat_columns = num ++ cats.map(_ + "_index")
val index = columns.filter(x => cat_columns.contains(x._1)).map(_._2)    // convert list of strings to list of indices to pass to row

// integer index of label
val label_index = columns.filter(_._1 == "Response_index").map(_._2).head

// construct the (label, feature) LabeledPoint RDD
val censusRDD = census_cats.map(row =>
        LabeledPoint(row.getDouble(label_index), toVector(row, index))
        )

val censusDF = censusRDD.toDF


