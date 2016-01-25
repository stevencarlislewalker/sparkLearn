import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.feature.{StringIndexer, IndexToString}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
import sqlContext.implicits._
import org.apache.spark.mllib.regression.LabeledPoint

// This script is meant to get a data frame into shape so that it can be fed into an mllib model. That means that all categorical data (strings) are encoded as doubles - Eg., ("Yes"/ "No") ==> (0/1). And that it ends up as a data frame of LabeledPoints.

// the starting data frame is named census. Assumes we already have this data frame in spark-shell.

// names of columns we want to select for. Change these if the data frame is different or you want to select different columns.
val cats = List("workclass", "education", "marital-status", "sex") //categorical
val num = List("age") // numeric (continuous)
val label = "Response" // dependent variable

// helper functions

// index a categorical column from a data frame and append to the end of the data frame
// return a data frame
def indexCategorical(arg: String, df:DataFrame) : DataFrame = {
      val indexer = new StringIndexer()
      indexer.setInputCol(arg).setOutputCol(arg + "_index")
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




// convert all categories (including label) to numeric
val census_cats = (label :: cats).foldLeft(census)((df, arg) => indexCategorical(arg, df))

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

// the output data frame that we can build models on
val censusDF = censusRDD.toDF


