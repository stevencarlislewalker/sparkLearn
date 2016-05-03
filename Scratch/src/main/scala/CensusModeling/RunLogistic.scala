package CensusModeling

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.ml.attribute.{Attribute, AttributeGroup}
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier, LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.tree.angoss.{VisualizeTree, AngossNode}
import org.apache.spark.sql.{SQLContext, DataFrame}

/**
  * Created by brifkind on 27/01/2016.
  */
object RunLogistic {

  System.setProperty("hadoop.home.dir", "c:\\Users\\brifkind\\Winutils\\")
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)


  val conf = new SparkConf().setAppName("Sample Application").setMaster("local[2]")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)


  def runLogisticRegression(train: DataFrame, featureName: String = "features", labelName: String = "label"): LogisticRegressionModel = {
    val model = (new LogisticRegression()).setFeaturesCol(featureName).setLabelCol(labelName).fit(train)
    println("features are: " + AttributeGroup.fromStructField(train.schema("features")))
    println("label is: " + Attribute.fromStructField(train.schema("Response_index")))
    println("The data frame looks like: ")
    println(train.show(5))
    println("coefficients are: " + model.coefficients)
    println("intercept is: " + model.intercept)
    println("-----------------------------------------------------")
    model
  }


  def main(args: Array[String]) {

    // read in csv file
    val filePath: String = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\data\\"
    val file = "smallCatTree.csv" // readLine("What is the name of the file? ")
    val data = new ReadInCSV(filePath, file).readInCsvEasy(sqlContext)
    println(data.show(5))
    println(data.schema)

    // request information about variables and number of times to run
    val independents = List("f1", "f2") // readLine("What are the independent variables? ").split(" ").toList
    val label = "label" // readLine("What is the dependent variable? ").toString


    // build a feature, label data frame
    // for decision tree
    val dataTransform = new DataTransform(independents, label, data)
    val train = dataTransform.runIndexed
    println("Attributes are: " + AttributeGroup.fromStructField(train.schema("features")))

    val numTimes = 1 //readLine("How many times to run? ").toInt
    val times = (1 to numTimes).map(_ => {
        val s1 = System.currentTimeMillis()
        val model = runLogisticRegression(train, "features", label + "_index")
        val s2 = System.currentTimeMillis()
        println("It took time = " + (s2 - s1))
        println("-------------------------------")
        s2 - s1
      })

    times.foreach(x => print(x + ", "))
  }

}
