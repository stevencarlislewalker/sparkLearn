// Build a decision tree. Assumes that we have a data frame in the correct form. Specifically of the form (label, features) with those names corresponding to those columns. The pipeline of scripts to run is readInCSV.scala, then catToNumerica.scala, and then this.

import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.classification.DecisionTreeClassificationModel

// df is the name of the data frame we are building a model on. Use the censusDF frame constructed in catToNumeric.
val df = censusDF

// Train a DecisionTree model.
val dt = new DecisionTreeClassifier()
  .setLabelCol("label")
  .setFeaturesCol("features")
val dt_model = dt.fit(df)