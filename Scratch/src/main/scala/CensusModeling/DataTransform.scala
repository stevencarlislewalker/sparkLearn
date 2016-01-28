package CensusModeling

/**
  * Created by brifkind on 26/01/2016.
  */

import org.apache.spark.ml.feature.{OneHotEncoder, StringIndexer, VectorAssembler}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StringType

class DataTransform(val independents: List[String] = List("age"),
                    val label: String = "Response",
                    val data: DataFrame) {


  // categorical independent variables
  val cats: Seq[String] = {
    data.schema
      .filter(sf => sf.dataType.equals(StringType) && independents.contains(sf.name))
      .map(_.name)
  }
  // numerical independent variables
  val num = data.schema
    .filter(sf => !(sf.dataType.equals(StringType)) && independents.contains(sf.name)).map(_.name)


  // convert all categories (including label) to numeric
  // index all input categorical columns from the data frame and append to the end of the data frame
  // return a data frame
  def indexCategorical(df: DataFrame, ending:String): DataFrame = {

    def oneIndexCategorical(df_temp: DataFrame, arg: String): DataFrame = {
      val indexer = new StringIndexer()
      indexer.setInputCol(arg).setOutputCol(arg + ending)
      indexer.fit(df).transform(df_temp)
    }

    // all categorical variables including response indexed
    (label :: cats.toList).foldLeft(df)((df_temp, arg) => oneIndexCategorical(df_temp, arg))

  }

  /*
  convert all indexed categories to one of k encoding
   */
  def oneHotCategorical(df: DataFrame, from:String, to:String): DataFrame = {

    def oneHot(df_temp: DataFrame, arg: String): DataFrame = {
      val onehot = new OneHotEncoder()
      onehot.setInputCol(arg + from).setOutputCol(arg + to)
      onehot.transform(df_temp)
    }
    (cats.toList).foldLeft(df)((df_temp, arg) => oneHot(df_temp, arg))
  }

  // construct the (label, feature) LabeledPoint data frame using VectorAssembler
  // only keep the columns we selected for
  def applyVectorAssembler(df_indexed: DataFrame,
                           from:String): DataFrame = {

    val indep_index: Seq[String] = cats.map(_ + from) ++ num

    // vector assembler
    val assembler = new VectorAssembler()
      .setInputCols(indep_index.toArray)
      .setOutputCol("features")
    assembler.transform(df_indexed)
  }

  def runIndexed: DataFrame = {
    val indexed = indexCategorical(data, "_index")
    val train = applyVectorAssembler(indexed, "_index")
    train
  }

  def runEncoded: DataFrame = {
    val indexed = indexCategorical(data, "_index")
    val encoded = oneHotCategorical(indexed, "_index", "_encoded")
    val train = applyVectorAssembler(encoded, "_encoded")
    train
  }


}
