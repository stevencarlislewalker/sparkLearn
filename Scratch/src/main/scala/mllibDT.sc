import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.ml.tree.InternalNode
import org.apache.spark.ml.tree.Node

def printAllNodes(root:Node):Unit={
  root match {
    case n:InternalNode => {
      println(n.toString())
      printAllNodes(n.leftChild)
      printAllNodes(n.rightChild)
    }
    case None => ""
  }
}