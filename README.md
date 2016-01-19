# sparkLearn

Figuring out logistic regression and decision trees in spark.

#### Resources

[hadoop shell](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/FileSystemShell.html)

[good ebook maybe](https://jaceklaskowski.gitbooks.io/mastering-apache-spark/)

```
If this is your first time using the Spark shell (or any Scala REPL, for that matter), you
should run the :help command to list available commands in the shell. :history
and :h? can be helpful for finding the names that you gave to variables or functions
that you wrote during a session but can’t seem to find at the moment. :paste can help
you correctly insert code from the clipboard—something you may well want to do
while following along with the book and its accompanying source code.
```

#### Misc problems

* Endless [Application report](http://stackoverflow.com/questions/30828879/application-report-for-application-state-accepted-never-ends-for-spark-submi) -- FIXME: try restarting all cloudera services

#### Get spark-shell working

##### Cloudera

DONE (for steve not ben)

outline of how we did it:

1. cloudera virtual box
2. comes with lots of big data tools, including spark etc...
3. `spark-shell` is on the path (not `spark`)
4. if we can't find `sqlContext`, diagnose with cloudera browser-based tools
5. where's `scala`??

##### DIY (e.g. from some ubuntu thing)

FIXME: in progress

#### Get census into a data frame

1. Manage the distributed file system -- e.g. [putCensusOnHDFS](https://github.com/stevencarlislewalker/sparkLearn/blob/master/snipets/putCensusOnHDFS)
2. Get CSV from distributed file system into an RDD [loadCensus.scala](https://github.com/stevencarlislewalker/sparkLearn/blob/master/snipets/loadCensus.scala)
3. (TODO) Convert RDD to DataFrame (e.g. http://spark.apache.org/docs/latest/sql-programming-guide.html#programmatically-specifying-the-schema)

##### Using `spark-csv` library

[spark-csv](https://github.com/databricks/spark-csv)

The basic idea is to call `spark-shell` with the `--packages` option.

Ben had a success here, but it hasn't been documented yet (TODO).

#### Development environment / workflow setup

##### Use Eclipse

Don't know how to incorporate spark and scala from centos with eclipse and its scala plugin but this a very helpful [writeup](http://www.nodalpoint.com/development-and-deployment-of-spark-applications-with-scala-eclipse-and-sbt-part-1-installation-configuration/) on how to set up a development framework using eclipse, scala, spark, and sbt. It requires a fresh install of spark -- seems unnecessary since spark is already installed on centos.

**Sbt**:
sbt stands for simple build tool - I believe it's a way of building scala (and maybe spark) projects. Have to specify dependencies and structure.

The website is http://www.scala-sbt.org/0.13/docs/Installing-sbt-on-Linux.html. Centos is redhat so just run

curl https://bintray.com/sbt/rpm/rpm | sudo tee /etc/yum.repos.d/bintray-sbt-rpm.repo

sudo yum install sbt



#### Writing compiled Spark programs

[spark-submit](http://spark.apache.org/docs/latest/submitting-applications.html)

1. What we want from this:  Make deployed Spark apps
2. Task:  understand dependencies for building spark/scala standalones (i.e. what is spark exactly?  is it just a 'package' for scala?



#### Load in our test data set

* (DONE) We only need one -- Census.
* (DONE) Can we just drag and drop to the VM?  I think so -- yes definitely. [http://www.howtogeek.com/187535/how-to-copy-and-paste-between-a-virtualbox-host-machine-and-a-guest-machine/]
* Do this as a spark DataFrame?

#### Try out logistic regression

