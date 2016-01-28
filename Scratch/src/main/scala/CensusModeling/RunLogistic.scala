package CensusModeling

import org.apache.spark.ml.attribute.{Attribute, AttributeGroup}
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.sql.DataFrame

/**
  * Created by brifkind on 27/01/2016.
  */
object RunLogistic {

  def runLogisticRegression(independents:List[String],
                            label:String,
                            data: DataFrame): LogisticRegressionModel = {
    val logT = new DataTransform(independents, label, data)
    val train = logT.runEncoded

    val model = (new LogisticRegression()).setLabelCol(label + "_index").fit(train)
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
    val filePath: String = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\data\\Census.csv"
    val data = (new ReadInCSV(filePath)).readInCsvEasy

    // request information about variables and number of times to run
    val independents = List("age", "sex") //readLine("What are the independent variables? ").split(" ").toList
    val label = "Response" //readLine("What is the dependent variable? ").toString

    val numTimes = 1 //readLine("How many times to run? ").toInt
    val times = (1 to numTimes).map(_ => {
      val s1 = System.currentTimeMillis()
      val model = runLogisticRegression(independents, label, data)
      val s2 = System.currentTimeMillis()
      println("It took time = " + (s2 - s1))
      println("-------------------------------")
      s2 - s1
    })

    times.foreach(x=>print(x + ", "))
  }

}
