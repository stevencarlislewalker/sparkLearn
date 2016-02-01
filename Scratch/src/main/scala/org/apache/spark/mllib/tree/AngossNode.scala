package org.apache.spark.ml.tree.angoss

import java.io.{BufferedWriter, File, FileWriter}

import org.apache.spark.ml.tree._
import scala.sys.process._

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
          if ((nodeIndex & (1 << levelsToGo - 1)) == 0) {
            println("went left")
            new InternalNode(
              node.prediction,
              node.impurity,
              node.gain,
              deleteNode(node.leftChild, nodeIndex, levelsToGo - 1),
              node.rightChild,
              node.split,
              node.impurityStats
            ) //construct new right subtree
          }
          else {
            println("went left")
            new InternalNode(
              node.prediction,
              node.impurity,
              node.gain,
              node.leftChild,
              deleteNode(node.rightChild, nodeIndex, levelsToGo - 1),
              node.split,
              node.impurityStats
            ) // construct new left subtree
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
          if ((nodeIndex & (1 << levelsToGo - 1)) == 0) {
            println("went left")
            new InternalNode(
              node.prediction,
              node.impurity,
              node.gain,
              addNode(node.leftChild, newNode, nodeIndex, levelsToGo - 1),
              node.rightChild,
              node.split,
              node.impurityStats
            ) //construct new right subtree
          }
          else {
            println("went left")
            new InternalNode(
              node.prediction,
              node.impurity,
              node.gain,
              node.leftChild,
              addNode(node.rightChild, newNode, nodeIndex, levelsToGo - 1),
              node.split,
              node.impurityStats
            ) // construct new left subtree
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

  def toDotString(prefix: String, node: Node, index: Int): String = {
    node match {
      case (n: InternalNode) => {
        val s = n.split match {
          case (s: CategoricalSplit) => s.leftCategories
          case (s: ContinuousSplit) => s.threshold
        }
        val left = index.toString + " -> " + (index << 1) + " [labeldistance=2.5, labelangle=45] ;\n"
        val right = index.toString + " -> " + ((index << 1) + 1) + " [labeldistance=2.5, labelangle=-45] ;\n"
        val label = index + """ [label="""" + s + """"] ;""" + "\n"
        val newPrefix = prefix + label + left + right
        println(newPrefix)
        val leftPrefix = toDotString(newPrefix, n.leftChild, index << 1)
        toDotString(leftPrefix, n.rightChild, (index << 1) + 1)
      }
      case (n: LeafNode) => {
        val newPrefix = prefix + index + """ [label="""" + n.prediction + """"] ;""" + "\n"
        println(newPrefix)
        newPrefix
      }
    }
  }

  def writeToDot(node: Node, fileName: String): Unit = {
    val dot = toDotString("", node, 1)
    val tree = "digraph Tree {\n node [shape=box] ;\n" + dot + "}"
    // FileWriter
    val file = new File(fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(tree)
    bw.close()
  }


  def writeToPng(node: Node, fileName: String): Unit = {
    writeto
  }

  def convertAndOpen(filePath: String = "\"C:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\treeDump\\",
                     node: Node): Unit = {
    writeToDot(node, filePath + "temp")

    val convertFile =
      "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe -Tpng " +
        filePath + "temp" + " -o " + filePath + "temp.png"
    convertFile.!

    val openFile =
      "rundll32 \"C:\\Program Files (x86)\\Windows Photo Viewer\\PhotoViewer.dll\", ImageView_Fullscreen " +
        filePath + "temp.png"
    openFile.!
  }

}

