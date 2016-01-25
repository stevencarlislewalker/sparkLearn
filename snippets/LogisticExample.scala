// Build a logistic regression. Assumes that we have a data frame in
// the correct form. Specifically of the form (label, features) with
// those names corresponding to those columns. The pipeline of scripts to
// run is readInCSV.scala, then catToNumerica.scala, and then this.

import org.apache.spark.ml.classification.LogisticRegression

// df is the name of the data frame we are building a model on. Use
// the censusDF frame constructed in catToNumeric

val df = censusDF

// setting column names for regression. defaults are: (label,
// features) for (dependent, independent). These are also the defaults
// when we mapped RDD of LabeledPoints to dataframe.

val lr = new LogisticRegression()
    .setLabelCol("Response_index")
    .setFeaturesCol("features")
val model = lr.fit(df)
