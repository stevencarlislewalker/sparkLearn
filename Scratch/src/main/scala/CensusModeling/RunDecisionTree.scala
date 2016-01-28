package CensusModeling

import org.apache.spark.ml.attribute.{Attribute, AttributeGroup}
import org.apache.spark.ml.classification.{DecisionTreeClassifier, DecisionTreeClassificationModel}
import org.apache.spark.sql.DataFrame

/**
  * Created by brifkind on 28/01/2016.
  */
object RunDecisionTree {


  def runDecisionTree(independents: List[String],
                      label: String,
                      data: DataFrame): DecisionTreeClassificationModel = {
    val logT = new DataTransform(independents, label, data)
    val train = logT.runEncoded

    val model = new DecisionTreeClassifier().setLabelCol(label + "_index").fit(train)
    println("features are: " + AttributeGroup.fromStructField(train.schema("features")))
    println("label is: " + Attribute.fromStructField(train.schema("Response_index")))
    println("The data frame looks like: ")
    println(train.show(5))
    println("Tree is: " + model.toDebugString)
    println("-----------------------------------------------------")
    model
  }


  def main(args: Array[String]) {

    // read in csv file
    val filePath: String = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\data\\Census.csv"
    val data = (new ReadInCSV(filePath)).readInCsvEasy

    // request information about variables and number of times to run
    val independents = List("age", "sex") //readLine("What are the independent variables? ").split(" ").toList
    val label = "Response" //readLine("What is the dependent variable? ").toString

    val model = runDecisionTree(independents, label, data)

  }


}
