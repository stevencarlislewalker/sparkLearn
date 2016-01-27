import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}

import scala.collection.immutable.IndexedSeq

/**
  * Created by brifkind on 27/01/2016.
  */
object RunLogistic {

  def runLogisticRegression(ct:CensusTransform): LogisticRegressionModel = {
    val data = ct.readInCsvEasy
    val data_index = ct.indexCategorical(data)
    val train = ct.applyVectorAssembler(data_index)
    (new LogisticRegression()).setLabelCol(ct.label + "_index").fit(train)
  }


  def main(args: Array[String]) {
    val filePath: String = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\data\\TripleCensus.csv"
    val ct = new CensusTransform(filePath = filePath)

    val times = (1 to 8).map(_ => {
      val s1 = System.currentTimeMillis()
      val model = runLogisticRegression(ct)
      val s2 = System.currentTimeMillis()
      println("coefficients are: " + model.coefficients)
      println("It took time = " + (s2 - s1))
      println("-------------------------------")
      s2 - s1
    })

    times.foreach(x=>print(x + ", "))
  }

}
