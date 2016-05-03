package org.apache.spark.ml.tree.angoss

import java.io.{BufferedWriter, File, FileWriter}

import org.apache.spark.ml.tree._
import org.apache.spark.mllib.tree.impurity.ImpurityCalculator


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
    * Returns a new subtree with specified nodes subtrees deleted.
    */

  def deleteNode(rootNode: Node, nodeIndex: Int, levelsToGo: Int): Node = {
    if (levelsToGo == 0) new LeafNode(rootNode.prediction, rootNode.impurity, rootNode.impurityStats)
    else {
      rootNode match {
        case (node: InternalNode) => {
          // copy node except can change children
          def mkNode = new InternalNode(node.prediction, node.impurity, node.gain,
            _: Node, _: Node,
            node.split, node.impurityStats)

          if ((nodeIndex & (1 << levelsToGo - 1)) == 0) {
            //construct new right subtree
            mkNode(deleteNode(node.leftChild, nodeIndex, levelsToGo - 1), node.rightChild)
          }
          else {
            //construct new left subtree
            mkNode(node.leftChild, deleteNode(node.rightChild, nodeIndex, levelsToGo - 1))
          }
        }
        case (node: LeafNode) => throw new IndexOutOfBoundsException("element not in tree")
      }
    }
  }

  /**
  Adds a new subtree a the specified index. Replaces the node at nodeIndex with newNode
    * */
  def addNode(rootNode: Node, newNode: Node, nodeIndex: Int, levelsToGo: Int): Node = {
    if (levelsToGo == 0) newNode
    else {
      rootNode match {
        case (node: InternalNode) => {

          // copy node except can change children
          def mkNode = new InternalNode(node.prediction, node.impurity, node.gain,
            _: Node, _: Node,
            node.split, node.impurityStats)

          if ((nodeIndex & (1 << levelsToGo - 1)) == 0) {
            // went left
            mkNode(addNode(node.leftChild, newNode, nodeIndex, levelsToGo - 1), node.rightChild)
          }
          else {
            // went right
            mkNode(node.leftChild, addNode(node.rightChild, newNode, nodeIndex, levelsToGo - 1))
          }
        }
        case (node: LeafNode) => throw new IndexOutOfBoundsException("element not in tree")
      }
    }
  }

  def printNode(prefix: String, node: Node) {
    node match {
      case (n: InternalNode) => {
        println(prefix + "-" + {
          n.split match {
            case (s: CategoricalSplit) => s.leftCategories
            case (s: ContinuousSplit) => s.threshold
          }
        })
        println(prefix + " " + "|")
        printNode(prefix + " |", n.leftChild)
        println(prefix + " |")
        printNode(prefix + " |", n.rightChild)
      }
      case (n: LeafNode) => println(prefix + "-" + n.prediction)
    }
  }


  /** force split
    */
  def forceSplit(node:InternalNode, nodeIndex:Int):Array[Double] ={
    val calc: ImpurityCalculator = node.impurityStats
    calc.stats
  }

}




