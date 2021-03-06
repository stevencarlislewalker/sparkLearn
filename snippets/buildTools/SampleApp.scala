/**
  * Created by brifkind on 26/01/2016.
  */
/* SampleApp.scala:
   This application simply counts the number of lines that contain "val" in this document
 */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object SampleApp {
  def main(args: Array[String]) {
    
    // Only on windows
    // val winutilsPath = "c:\\Users\\brifkind\\Winutils\\" \\CHANGE ME
    // System.setProperty("hadoop.home.dir", )
    
    val txtFile = "c:\\Users\\brifkind\\Documents\\Angoss\\sparkLearn\\Scratch\\src\\main\\scala\\SampleApp.scala" \\ Change to your the filepath of this directory
    
    val conf = new SparkConf().setAppName("Sample Application").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val txtFileLines = sc.textFile(txtFile , 2).cache()
    val numAs = txtFileLines.filter(line => line.contains("val")).count()
    println("Lines with val: %s".format(numAs))
  }
}