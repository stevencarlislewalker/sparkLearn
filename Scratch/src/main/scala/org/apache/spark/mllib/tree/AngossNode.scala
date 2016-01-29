package org.apache.spark.mllib.tree.model.angoss

import org.apache.spark.mllib.tree.model.Split
import org.apache.spark.mllib.tree
import org.apache.spark.mllib.tree.configuration.Algo.Algo
import org.apache.spark.mllib.tree.configuration.Strategy
import org.apache.spark.mllib.tree.model.{InformationGainStats, Predict, Node, DecisionTreeModel}


//class Node(id: Int,
//           predict: Predict,
//           impurity: Double,
//           isLeaf: Boolean,
//           split: Option[Split],
//           leftNode: Option[Node],
//           rightNode: Option[Node],
//           stats: Option[InformationGainStats]) extends
//Node(id, predict, impurity, isLeaf, split, leftNode, rightNode, stats) {


  object AngossNode {
  /**
    * Returns a new subtree with specified nodes deleted
    */
  import org.apache.spark.mllib.tree.model.Node

  def deleteNode(startNode:Option[Node], index: Int): Node = {
    startNode match {
      case Some(node) => {
        val dir = index % 2
        if (dir == 0) {
          val leftNode = deleteNode(node.leftNode, index )
        }
      }
    }

    val leftNodeCopy = if (leftNode.isEmpty || index.contains(leftNode.get.id)) {
      None
    } else {
      Some(leftNode.get.deleteNode(index: _*))
    }
    val rightNodeCopy = if (rightNode.isEmpty || index.contains(rightNode.get.id)) {
      None
    } else {
      Some(rightNode.get.deleteNode(index: _*))
    }
    new Node(startid, predict, impurity, isLeaf, split, leftNodeCopy, rightNodeCopy, stats)
  }


  def deleteSubTree(index: Int): Node = {
    deleteNode(Node.leftChildIndex(index), Node.rightChildIndex(index))
  }

  //  class AngossNode extends Node {
  //  }

}

/**
  * Created by brifkind on 29/01/2016.
  */
class aDecisionTreeModel(topNode:Node, algo:Algo)
  extends DecisionTreeModel(topNode, algo) {

  def printTree (prefix: String, node: Option[Node] ) {
    node match {
      case Some (n) => {
        println (prefix + "-" + n.id)
        println (prefix + " " + "|")
        printTree (prefix + " |", n.leftNode)
        printTree (prefix + " |", n.rightNode)
      }
      case _ =>
    }
  }

  override def toString: Unit = printTree ("", Option (topNode) )

}

