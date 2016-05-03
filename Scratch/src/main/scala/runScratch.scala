import CensusModeling.{DataTransform, ReadInCSV}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.configuration.{Algo, Strategy}
import org.apache.spark.mllib.tree.impurity.Gini
import org.apache.spark.mllib.tree.model.Node
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SQLContext, DataFrame}
import org.apache.spark.ml.classification.{DecisionTreeClassifier, DecisionTreeClassificationModel}
import scala.sys.process._

/**
  * Created by brifkind on 28/01/2016.
  */

object runScratch {

  System.setProperty("hadoop.home.dir", "c:\\Users\\brifkind\\Winutils\\")
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)


  val conf = new SparkConf().setAppName("Sample Application").setMaster("local[2]")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)


  def runCode: DataFrame = {

    //    System.setProperty("hadoop.home.dir", "c:\\Users\\brifkind\\Winutils\\")
    //    Logger.getLogger("org").setLevel(Level.OFF)
    //    Logger.getLogger("akka").setLevel(Level.OFF)
    //    val conf = new SparkConf().setAppName("Sample Application").setMaster("local[2]")
    //    val sc = new SparkContext(conf)
    //    val sqlContext = new SQLContext(sc)

    //    // Load the data stored in LIBSVM format as a DataFrame.
    //    val filePath: String = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\data\\sample_libsvm_data.txt"
    //    val data = sqlContext.read.format("libsvm").load(filePath)
    val data = new ReadInCSV(file = "smallRandomCatTree.csv").readInCsvEasy(sqlContext)
    val train = new DataTransform(List("f1", "f2"), "label", data).runIndexed
//
//
//
//    val strat: Strategy = new Strategy(algo = Algo.Classification,
//      impurity = Gini, maxDepth = 5, numClasses = 2, maxBins = 6,
//      categoricalFeaturesInfo = Map(0 -> 6, 1 -> 2))
//    val dt = new DecisionTree(strat)
    train
  }

  def dfToRdd(df: DataFrame): RDD[LabeledPoint] = {
    val rdd = df.select("Response_index", "features").map(row => LabeledPoint(row.getDouble(0), row.getAs[Vector](1)))
    rdd
  }

  def main(args: Array[String]) {
    val filePath: String = "C:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\treeDump\\"
    val convertFile =
      "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe -Tpng " +
       filePath + "temp" + " -o " + filePath + "temp.png"
    convertFile.!

    val openFile =
      "rundll32 \"C:\\Program Files (x86)\\Windows Photo Viewer\\PhotoViewer.dll\", ImageView_Fullscreen " +
        filePath + "temp.png"
    openFile.!
  }


}