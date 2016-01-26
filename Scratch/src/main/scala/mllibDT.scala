/**
  * Created by brifkind on 26/01/2016.
  */

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

object mllibDT{


  def loadData(sc:SparkContext, filename:String):RDD[LabeledPoint] = {
    // Load and parse the data file.
    MLUtils.loadLibSVMFile(sc, filename)
  }
  // Split the data into training and test sets (30% held out for testing)
  // val splits = data.randomSplit(Array(0.7, 0.3))

    //val (trainingData, testData) = (splits(0), splits(1))

  // Train a DecisionTree model.
  //  Empty categoricalFeaturesInfo indicates all features are continuous.
  def trainDF(data:RDD[LabeledPoint]):DecisionTreeModel = {
    val numClasses = 2
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "gini"
    val maxDepth = 5
    val maxBins = 32

    val model = DecisionTree.trainClassifier(data, numClasses, categoricalFeaturesInfo,
      impurity, maxDepth, maxBins)
    model
  }



  def main(args:Array[String]): Unit ={
    System.setProperty("hadoop.home.dir", "c:\\Users\\brifkind\\Winutils\\")
    val conf = new SparkConf().setAppName("Sample Application").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    val filePath = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\data\\sample_libsvm_data.txt"
    val data = mllibDT.loadData(sc, filePath)
    val model = mllibDT.trainDF(data)
    println(model.numNodes)
  }
//
//  // Evaluate model on test instances and compute test error
//  val labelAndPreds = testData.map { point =>
//    val prediction = model.predict(point.features)
//    (point.label, prediction)
//  }

}
