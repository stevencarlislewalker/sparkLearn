# Figuring out Spark -- one step at a time

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

#### Misc problems/solutions

* Endless [Application report](http://stackoverflow.com/questions/30828879/application-report-for-application-state-accepted-never-ends-for-spark-submi) -- FIXME: try restarting all cloudera services
* http://www.howtogeek.com/187535/how-to-copy-and-paste-between-a-virtualbox-host-machine-and-a-guest-machine/

#### Get spark-shell working

#### The easiest approach

https://github.com/stevencarlislewalker/sparkLearn/blob/master/notes/LocalSparkInstallOnUbuntu.md

##### Using the Cloudera framework

Ugh...

1. cloudera virtual box
2. comes with lots of big data tools, including spark etc...
3. `spark-shell` is on the path (not `spark`)
4. if we can't find `sqlContext`, diagnose with cloudera browser-based tools
5. where's `scala`??

##### Using `spark-csv` library

[spark-csv](https://github.com/databricks/spark-csv)

The basic idea is to call `spark-shell` with the `--packages` option. See [here](https://github.com/stevencarlislewalker/sparkLearn/blob/master/notes/Localspark-csv.md) for notes on how to do this on a local spark-shell.

#### Development environment / workflow setup

##### Using Eclipse

Don't know how to incorporate spark and scala from centos with eclipse and its scala plugin but this a very helpful [writeup](http://www.nodalpoint.com/development-and-deployment-of-spark-applications-with-scala-eclipse-and-sbt-part-1-installation-configuration/) on how to set up a development framework using eclipse, scala, spark, and sbt. It requires a fresh install of spark -- seems unnecessary since spark is already installed on centos.

##### Using Emacs

FIXME:  this is not quite right / not very general

```bash
git clone https://github.com/scala/scala-tool-support.git
export PATH=$PATH:~/scala/build/quick/bin
```

```lisp
(add-to-list 'load-path "~/scala-dist/scala-tool-support/tool-support/emacs")
(add-to-list 'load-path "~/scala/build/quick/bin")
(require 'scala-mode-auto)
```
