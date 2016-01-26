/**
  * Created by Ben on 2016-01-25.
  */

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.ml.feature.{VectorAssembler, StringIndexer}
import org.apache.spark.sql.{SQLContext, DataFrame}
//import sqlContext.implicits._

class ScratchWork(val catList:List[String], val numList:List[String], val label:String) extends App{


  def indexCategorical(arg: String, df:DataFrame) : DataFrame = {
    val indexer = new StringIndexer()
    indexer.setInputCol(arg).setOutputCol(arg + "_index")
    indexer.fit(df).transform(df)
  }

  override def main(args:Array[String]): Unit ={
    val conf = new SparkConf().setAppName("Sample Application").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val cats = List("workclass", "education", "marital-status", "sex") //categorical
    val num = List("age") // numeric (continuous)
    val label = "Response" // dependent variable

    val scratch = new ScratchWork(cats, num, label)


    // CHANGE to file path of CSV file of your choice
    val filePath = "/home/ben/Documents/phoenix/sparkLearn/data/Census.csv"

    val census = sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(filePath)

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


  }

}
