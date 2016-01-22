import org.apache.spark.sql.DataFrame

def age_new() = census.select("age", "Response")
    .map(row => LabeledPoint(row.getString(1), Vectors.dense(row.getInt(0)))).toDF("response", ageVRBinary.columns(0))


def catToNumber(arg: String, df:DataFrame) : DataFrame = {
      val indexer = new StringIndexer().setInputCol(arg).setOutputCol(arg+"_index")
      indexer.fit(df).transform(df)
}

val cats = List("workclass", "education", "marital-status", "sex")

val small_census = census.select("age", "workclass", "education", "marital-status", "sex", "Response")

val small_census_cats = cats.foldLeft(small_census)((df, arg) => catToNumber(arg, df)).
    select("age" :: cats.map(x=>x+"_index"))


val a = Array("age", "workclass", "education")
