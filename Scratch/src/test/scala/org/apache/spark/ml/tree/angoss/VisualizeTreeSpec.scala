package org.apache.spark.ml.tree.angoss

import CensusModeling.{DataTransform, ReadInCSV}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.ml.attribute.AttributeGroup
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.Stack

/**
  * Created by brifkind on 02/02/2016.
  */

class VisualizeTreeSpec extends FlatSpec with Matchers {

  System.setProperty("hadoop.home.dir", "c:\\Users\\brifkind\\Winutils\\")
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)


  val conf = new SparkConf().setAppName("Sample Application").setMaster("local[2]")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)


  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be(2)
    stack.pop() should be(1)
  }

  "A file" should "open and close" in {
    val data = new ReadInCSV(file = "smallRandomCatTree.csv").readInCsvEasy(sqlContext)
    val train = new DataTransform(List("f1", "f2"), "label", data).runIndexed
    val model = (new DecisionTreeClassifier()).setFeaturesCol("features").setLabelCol("label_index").fit(train)

    val atts = AttributeGroup.fromStructField(train.schema("features"))
    val visualize = new VisualizeTree(atts=atts)

    visualize.convertAndOpen(model.rootNode, "treeA")

//    val treeB = AngossNode.deleteNode(model.rootNode, 6, 2)
//    visualize.writeToPng(treeB, "treeB")


//    val treeC = AngossNode.addNode(model.rootNode, treeB, 2, 1)
//    visualize.convertAndOpen(treeC, "treeC")


  }

}