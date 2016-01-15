# sparkLearn

Figuring out logistic regression and decision trees in spark.

#### Get spark-shell working

DONE

#### Development environment / workflow setup

Use Eclipse ... but how?  Do you know Ben?
Ben: I don't know how to incorporate spark and scala from centos with eclipse and its scala plugin.

sbt stands for simple build tool - I believe it's how scala (and maybe spark) projects get created. Have to specify dependencies and structure.

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


