import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}

val data = sqlContext.read.format("libsvm").load("/home/ben/Documents/phoenix/sparkLearn/data/sample_libsvm_data.txt")

def catToNumber(arg: String, df:DataFrame) : DataFrame = {
      val indexer = new StringIndexer().setInputCol(arg).setOutputCol(arg + "_index")
      indexer.fit(df).transform(df)
}

val cats = List("workclass", "education", "marital-status", "sex")
val small_census = census.select("age", "workclass", "education", "marital-status", "sex", "Response")
val small_census_cats = ("Response" :: cats).foldLeft(small_census)((df, arg) => catToNumber(arg, df))


// import org.apache.spark.ml.classification.DecisionTreeClassificationModel

import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.ml.classification.DecisionTreeClassifier

val ageDF = small_census_cats.map(row => LabeledPoint(row.getDouble(6), Vectors.dense(row.getInt(0)))).toDF()
val indexer = new StringIndexer().setInputCol("label").setOutputCol("label_index")
val ageDF2 = indexer.fit(ageDF).transform(ageDF)


val dt = new DecisionTreeClassifier()
  .setLabelCol("label_index")
  .setFeaturesCol("features")

val model_dt = dt.fit(ageDF2)