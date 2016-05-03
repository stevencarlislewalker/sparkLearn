package org.apache.spark.ml.tree.angoss

import java.io.{BufferedWriter, File, FileWriter}
import org.apache.spark.ml.attribute.{NominalAttribute, Attribute, AttributeGroup}
import org.apache.spark.ml.util.MetadataUtils

import scala.sys.process._
import org.apache.spark.ml.tree._

/**
  * Created by brifkind on 02/02/2016.
  */
class VisualizeTree(val filePath: String = "C:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\treeDump\\",
                    val atts: AttributeGroup) {


  //sloppy code. assumes all attributes exist
  // map of feature index to name of feature
  val idMap = atts.attributes.get.map(a => (a.index.get, a.name.get)).toMap
  // map of categorical feature index to corresponding list of categories
  val catMap = atts.attributes.get
    .flatMap(a => a match {
      case a: NominalAttribute => Some(a.index.get, a.values.get)
      case _ => None
    }).toMap


  def toDotString(prefix: String, node: Node, index: Int): String = {

    // helper method - add label to a node.
    def toLabel(args: String*): String = {
      " [label=\"" + args.mkString("\n") + "\"]"
    }

    node match {
      case (n: InternalNode) => {
        val splVal = n.split match {
          case (s: CategoricalSplit) => s.leftCategories
            .map(c => catMap(s.featureIndex)(c.toInt)).mkString(", ")
          case (s: ContinuousSplit) => s.threshold
        }
        val left = index.toString + " -> " + (index << 1) + " ;\n"
        val right = index.toString + " -> " + ((index << 1) + 1) + " ;\n"
        val label = index +
          toLabel(List("index=" + index.toString,
            "left=" + splVal.toString,
            "name=" + idMap(n.split.featureIndex)): _*) +
          " ; \n"
        val newPrefix = prefix + label + left + right
        val leftPrefix = toDotString(newPrefix, n.leftChild, index << 1)
        toDotString(leftPrefix, n.rightChild, (index << 1) + 1)
      }
      case (n: LeafNode) => {
        val newPrefix = prefix + index + " " +
          toLabel(List("index=" + index.toString,
            "prediction=" + n.prediction.toString): _*) +
          " ; \n"
        newPrefix
      }
    }
  }


  def writeToDot(node: Node, fileName: String): Unit = {

    val dot = toDotString("", node, 1)
    val tree = "digraph Tree {\n node [shape=box] ;\n" + dot + "}"

    // FileWriter
    val file = new File(filePath + fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(tree)
    bw.close()
  }


  def writeToPng(node: Node, fileName: String): Unit = {
    writeToDot(node, "temp")
    val convertFile =
      "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe -Tpng " +
        filePath + "temp" + " -o " + filePath + fileName + ".png"
    convertFile.!
    new File(filePath + "temp").delete()
  }

  def convertAndOpen(node: Node, fileName: String = "tree"): Unit = {
    writeToPng(node, fileName)

    val openFile =
      "rundll32 \"C:\\Program Files (x86)\\Windows Photo Viewer\\PhotoViewer.dll\", ImageView_Fullscreen " +
        filePath + fileName + ".png"
    openFile.!
  }


}
