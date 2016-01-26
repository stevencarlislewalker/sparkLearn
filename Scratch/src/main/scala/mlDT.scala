/**
  * Created by brifkind on 26/01/2016.
  */

import org.apache.spark.ml.tree.{LeafNode, InternalNode, Node}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.feature.{StringIndexer, IndexToString}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.sql.SQLContext

class mlDT(val cats: List[String], val num: List[String], val label: String, filePath:String) {




  def readInCsv(filePath: String, sqlContext: SQLContext): DataFrame = {
    sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(filePath)
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

    (label :: cats).foldLeft(df)((df_temp, arg) => oneIndexCategorical(arg, df_temp))
  }


  // construct the (label, feature) LabeledPoint data frame using VectorAssembler
  // only keep the columns we selected for
  def applyVectorAssembler(df_indexed: DataFrame): DataFrame = {
    // independent variable column names (categoricals that have been converted to doubles)
    val indep_index = cats.map(_ + "_index") ++ num

    // vector assembler
    val assembler = new VectorAssembler()
      .setInputCols(indep_index.toArray)
      .setOutputCol("features")

    assembler.transform(df_indexed.select(label + "_index", indep_index: _*))
  }


  def runThis:DataFrame={
    val cats = List("workclass", "education", "marital-status", "sex") //categorical
    val num = List("age") // numeric (continuous)
    val label = "Response" // dependent variable
    val filePath = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\data\\Census.csv"

    System.setProperty("hadoop.home.dir", "c:\\Users\\brifkind\\Winutils\\")
    val conf = new SparkConf().setAppName("Sample Application").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    val data = readInCsv(filePath, sqlContext)
    val data_index = indexCategorical(data)
    applyVectorAssembler(data_index)
  }

  def printAllNodes(root:Node):Unit={
    root match {
      case n:InternalNode => {
        println(n.toString())
        printAllNodes(n.leftChild)
        printAllNodes(n.rightChild)
      }
      case n:LeafNode =>
    }
  }

}
