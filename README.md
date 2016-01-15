# sparkLearn

Figuring out logistic regression and decision trees in spark.

#### Get spark-shell working

DONE

#### Development environment / workflow setup

Use Eclipse ... but how?  Do you know Ben?

Ben: I don't know how to incorporate spark and scala from centos with eclipse and its scala plugin but this a very helpful [writeup](http://www.nodalpoint.com/development-and-deployment-of-spark-applications-with-scala-eclipse-and-sbt-part-1-installation-configuration/#comments) on how to set up a development framework using eclipse, scala, spark, and sbt. It requires a fresh install of spark -- seems unnecessary since spark is already installed on centos.

Sbt:
sbt stands for simple build tool - I believe it's a way of building scala (and maybe spark) projects. Have to specify dependencies and structure.

The website is http://www.scala-sbt.org/0.13/docs/Installing-sbt-on-Linux.html. Centos is redhat so just run

curl https://bintray.com/sbt/rpm/rpm | sudo tee /etc/yum.repos.d/bintray-sbt-rpm.repo

sudo yum install sbt



#### Writing compiled Spark programs

* `spark-submit`
* Need `sbt` (FIXME:  yum command?)

#### Load in our test data set

* We only need one -- Census.
* Can we just drag and drop to the VM?
* Do this as a spark DataFrame?

#### Try out logistic regression


