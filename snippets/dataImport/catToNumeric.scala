import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.feature.{StringIndexer, IndexToString}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.features.VectorAssembler
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
import sqlContext.implicits._
import org.apache.spark.mllib.regression.LabeledPoint

// This script is meant to get a data frame into a form that can be
// fed into an mllib model. That means that all categorical data
// (strings) are encoded as doubles - Eg., ("Yes"/ "No") ==> (0/1). And
// that it ends up as a data frame of LabeledPoints.

// the starting data frame is named census which we constructed in
// readInCSV.scala. Assumes we already have this data frame in
// spark-shell.

// names of columns we want to select for. Change these if the data
// frame is different than census or you want to select different
// columns.

val cats = List("workclass", "education", "marital-status", "sex") //categorical
val num = List("age") // numeric (continuous)
val label = "Response" // dependent variable

// Replaced by VectorAssembler
// takes a row, and integer index of column entries and returns a list of doubles corresponding to those entries
// def toVector(row:Row, index:List[Int]):Vector = {
//     val schema = row.schema

//     // extract features as doubles
//     val features = index.map(x=> schema(x).dataType match {
//     case IntegerType => row.getInt(x).toDouble
//     case DoubleType => row.getDouble(x)
//     })
//     Vectors.dense(features.head, features.tail:_*)
// }

// end helper functions

// convert all categories (including label) to numeric
// index a categorical column from a data frame and append to the end of the data frame
// return a data frame
def indexCategorical(arg: String, df:DataFrame) : DataFrame = {
      val indexer = new StringIndexer()
      indexer.setInputCol(arg).setOutputCol(arg + "_index")
      indexer.fit(df).transform(df)
}

//converted data frame
val census_cats = (label :: cats).foldLeft(census)((df, arg) => indexCategorical(arg, df))

// construct the (label, feature) LabeledPoint data frame using VectorAssembler
// only keep the label and feature columns

// independent variable column names (categoricals that have been converted to doubles)
val indep_index = cats.map(_ + "_index") ++ num

// vector assembler
val assembler = new VectorAssembler()
  .setInputCols(indep_index.toArray)
  .setOutputCol("features")

val censusDF = assembler.transform(census_cats.select(
		label + "_index", indep_index:_*))


