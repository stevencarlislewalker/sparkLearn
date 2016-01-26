# sparkLearn

Figuring out logistic regression and decision trees in spark.

#### Current tasks (for before Thursday?)

https://github.com/stevencarlislewalker/sparkLearn/issues

#### Resources

[Spark MLlib (1.6.0) Documentation](http://spark.apache.org/docs/latest/mllib-guide.html)    
[hadoop shell](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/FileSystemShell.html)  
[good ebook maybe](https://jaceklaskowski.gitbooks.io/mastering-apache-spark/)  
useful code bases  
- [mllib examples](https://github.com/apache/spark/tree/master/examples/src/main/scala/org/apache/spark/examples)
- [mllib source code](https://github.com/apache/spark/tree/v1.6.0/mllib/src/main/scala/org/apache/spark)  

Note that mllib is the base library and ml and mllib are subpackages of it.  
- ml is newer and "WILL BE" better than mllib. Supports dataframes and API is set up for pipelines but not as developed. 
- mllib is older. Supports RDDs. Development is shifting away from it but at the moment (1/2016) seems more developed.

[Logistic regression theory in the context of Spark](http://www.slideshare.net/dbtsai/2014-0620-mlor-36132297)

#### Misc problems

* Endless [Application report](http://stackoverflow.com/questions/30828879/application-report-for-application-state-accepted-never-ends-for-spark-submi) -- FIXME: try restarting all cloudera services

#### Get spark-shell working

#### The easiest approach

https://github.com/stevencarlislewalker/sparkLearn/blob/master/notes/LocalSparkInstallOnUbuntu.md

##### Using the Cloudera framework

1. cloudera virtual box
2. comes with lots of big data tools, including spark etc...
3. `spark-shell` is on the path (not `spark`)
4. if we can't find `sqlContext`, diagnose with cloudera browser-based tools
5. where's `scala`??

#### Get census into a data frame

1. Manage the distributed file system -- e.g. [putCensusOnHDFS](https://github.com/stevencarlislewalker/sparkLearn/blob/master/snipets/putCensusOnHDFS)
2. Get CSV from distributed file system into an RDD [loadCensus.scala](https://github.com/stevencarlislewalker/sparkLearn/blob/master/snipets/loadCensus.scala)
3. (TODO) Convert RDD to DataFrame (e.g. http://spark.apache.org/docs/latest/sql-programming-guide.html#programmatically-specifying-the-schema)

##### Using `spark-csv` library

[spark-csv](https://github.com/databricks/spark-csv)

The basic idea is to call `spark-shell` with the `--packages` option. See [here](https://github.com/stevencarlislewalker/sparkLearn/blob/master/notes/Localspark-csv.md) for notes on how to do this on a local spark-shell.

#### Development environment / workflow setup

##### Use Eclipse

Don't know how to incorporate spark and scala from centos with eclipse and its scala plugin but this a very helpful [writeup](http://www.nodalpoint.com/development-and-deployment-of-spark-applications-with-scala-eclipse-and-sbt-part-1-installation-configuration/) on how to set up a development framework using eclipse, scala, spark, and sbt. It requires a fresh install of spark -- seems unnecessary since spark is already installed on centos.

**Sbt**:
sbt stands for simple build tool - I believe it's a way of building scala (and maybe spark) projects. Have to specify dependencies and structure.

The website is http://www.scala-sbt.org/0.13/docs/Installing-sbt-on-Linux.html. Centos is redhat so just run

curl https://bintray.com/sbt/rpm/rpm | sudo tee /etc/yum.repos.d/bintray-sbt-rpm.repo

sudo yum install sbt

#### Load in our test data set

* (DONE) We only need one -- Census.
* (DONE) Can we just drag and drop to the VM?  I think so -- yes definitely. [http://www.howtogeek.com/187535/how-to-copy-and-paste-between-a-virtualbox-host-machine-and-a-guest-machine/]
* Do this as a spark DataFrame?

#### Try out logistic regression

#### Try out decision trees

#### Writing compiled Spark programs

[spark-submit](http://spark.apache.org/docs/latest/submitting-applications.html)

1. What we want from this:  Make deployed Spark apps
2. Task:  understand dependencies for building spark/scala standalones (i.e. what is spark exactly?  is it just a 'package' for scala?

