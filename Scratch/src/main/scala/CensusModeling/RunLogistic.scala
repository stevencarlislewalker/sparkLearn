package CensusModeling

import org.apache.spark.ml.attribute.{Attribute, AttributeGroup}
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier, LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.tree.angoss.AngossNode
import org.apache.spark.sql.DataFrame

/**
  * Created by brifkind on 27/01/2016.
  */
object RunLogistic {

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

  def runDecisionTree(train: DataFrame, featureName: String = "features", labelName: String = "label"): DecisionTreeClassificationModel = {
    val model = (new DecisionTreeClassifier()).setFeaturesCol(featureName).setLabelCol(labelName).fit(train)
    AngossNode.printNode("", model.rootNode)
    println("-----------------------------------------------")
    val filePath: String = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\pythonScratch\\"
    AngossNode.writeToDot(model.rootNode, filePath + "scalaTreeDot")

    val tree1 = AngossNode.deleteNode(model.rootNode, 6, 2)
    AngossNode.printNode("", tree1)
    println("-----------------------------------------------")
    val tree2 = AngossNode.addNode(model.rootNode, tree1, 2, 1)
    AngossNode.printNode("", tree2)

    model
  }


  def main(args: Array[String]) {

    // read in csv file
    val filePath: String = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\data\\"
    val file = "smallCatTree.csv" // readLine("What is the name of the file? ")
    val data = new ReadInCSV(filePath, file).readInCsvEasy
    println(data.show(5))
    println(data.schema)

    // request information about variables and number of times to run
    val independents = List("f1", "f2") // readLine("What are the independent variables? ").split(" ").toList
    val label = "label" // readLine("What is the dependent variable? ").toString


    // build a feature, label data frame
    val dataTransform = new DataTransform(independents, label, data)
    val train = dataTransform.runEncoded
    println("Attributes are: " + AttributeGroup.fromStructField(train.schema("features")))

    val numTimes = 1 //readLine("How many times to run? ").toInt
    val times = (1 to numTimes).map(_ => {
        val s1 = System.currentTimeMillis()
        val model = runDecisionTree(train, "features", label + "_index")
        val s2 = System.currentTimeMillis()
        println("It took time = " + (s2 - s1))
        println("-------------------------------")
        s2 - s1
      })

    times.foreach(x => print(x + ", "))
  }

}
