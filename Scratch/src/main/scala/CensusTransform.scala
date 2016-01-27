/**
  * Created by brifkind on 26/01/2016.
  */

import org.apache.spark.ml.classification.{LogisticRegressionModel, LogisticRegression}
import org.apache.spark.ml.tree.{LeafNode, InternalNode, Node}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.{Row, DataFrame, SQLContext}
import org.apache.spark.ml.feature.{StringIndexer, IndexToString}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.log4j.Logger
import org.apache.log4j.Level


import org.apache.spark.sql.types.{StructType, StructField}
import org.apache.spark.sql.types.{IntegerType, StringType, DataType}

import scala.collection.immutable.IndexedSeq

class CensusTransform(val independents: List[String] =
                      List("workclass", "education", "marital-status",
                        "occupation", "relationship", "sex", "hours-per-week",
                        "age", "num-products"),
                      val label: String = "Response",
                      val filePath: String = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\data\\Census.csv") {


  System.setProperty("hadoop.home.dir", "c:\\Users\\brifkind\\Winutils\\")
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  var columnNames: List[String] =
    List("Customer ID", "age", "workclass", "education", "marital-status", "occupation", "relationship",
      "sex", "hours-per-week", "Response", "capital-loss", "num-products", "capital-gain")
  var columnTypes: List[DataType] =
    List(IntegerType, IntegerType, StringType, StringType, StringType, StringType, StringType, StringType,
      IntegerType, StringType, IntegerType, IntegerType, IntegerType)


  val conf = new SparkConf().setAppName("Sample Application").setMaster("local[2]")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)


  def readInCsvEasy: DataFrame = {
    sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(filePath)
  }

  def readInCsvHard: DataFrame = {
    val df = sc.textFile(filePath)
    val schema = StructType(columnNames.map(name => StructField(name, StringType, true)))

    // Convert records of the RDD (people) to Rows.
    val rowRDD = df.map(_.split(",")).map(p => Row(p: _*))

    sqlContext.createDataFrame(rowRDD, schema)

  }


  // convert all categories (including label) to numeric
  // index all input categorical columns from the data frame and append to the end of the data frame
  // return a data frame
  def indexCategorical(df: DataFrame): DataFrame = {

    def oneIndexCategorical(arg: String, df: DataFrame): DataFrame = {
      val indexer = new StringIndexer()
      indexer.setInputCol(arg).setOutputCol(arg + "_index")
      indexer.fit(df).transform(df)
    }

    val schema = df.schema

    val cats:Seq[String] = schema.filter(sf => sf.dataType.equals(StringType) && independents.contains(sf.name)).map(_.name)
    (label :: cats.toList).foldLeft(df)((df_temp, arg) => oneIndexCategorical(arg, df_temp))
  }

  // construct the (label, feature) LabeledPoint data frame using VectorAssembler
  // only keep the columns we selected for
  def applyVectorAssembler(df_indexed: DataFrame): DataFrame = {
    // independent variable column names (categoricals that have been converted to doubles)

    val schema = df_indexed.schema
    val cats:Seq[String] = schema.filter(sf => sf.dataType.equals(StringType) && independents.contains(sf.name)).map(_.name)
    val num = schema.filter(sf => !(sf.dataType.equals(StringType)) && independents.contains(sf.name)).map(_.name)

    val indep_index:Seq[String] = cats.map(_ + "_index") ++ num

    // vector assembler
    val assembler = new VectorAssembler()
      .setInputCols(indep_index.toArray)
      .setOutputCol("features")
    assembler.transform(df_indexed.select(label + "_index", indep_index: _*))
  }




  def runThis: DataFrame = {
    val data = readInCsvEasy
    val data_index = indexCategorical(data)
    applyVectorAssembler(data_index)
  }

  def printAllNodes(root: Node): Unit = {
    root match {
      case n: InternalNode => {
        println(n.toString())
        printAllNodes(n.leftChild)
        printAllNodes(n.rightChild)
      }
      case n: LeafNode =>
    }
  }

}
