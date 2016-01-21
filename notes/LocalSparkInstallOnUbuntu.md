*Jan 2016*

Up and running with local instance of spark on ubuntu 15.10. Installing Java, Scala, and Spark. For a local instance of Spark there is no need to install Scala - Spark (prebuilt) comes with Scala (2.10.5). You do still need to install Java.  
This is all shamelessly adapted (stolen) from [this writeup](http://mbonaci.github.io/mbo-spark/)

####Instructions - installing java, scala, and spark on Ubuntu-15.10-desktop-amd64

#####Gotchas (errors Ben made):  
- Moving files sometimes requires sudo permissions. Need to change to root for those commands (`sudo -i`). Don't forget to change back right after (`su "username"`) since you don't want to edit root `PATH` and environment variables.  
- I just edited `~/.bashrc` to update `*_HOME` variables and the `PATH`. Don't know if this is best practice.    

#####Java (1.8.0_66) 
Note: non- Oracle version 'may' have issues with spark. Play it safe and download the Oracle version.  
`sudo add-apt-repository ppa:webupd8team/java`  
`sudo apt-get update`  
`sudo apt-get install oracle-java8-installer`  

#####Scala (2.11.7)
Download from [scala download page](http://www.scala-lang.org/download/install.html) and unpack. Then move somewhere safe - '/usr/local/share/' seems to be the default location for this kind of thing.  
`tar -xzf scalaVersionNumber.tar.gz`  
`mv scalaVersionNumber /usr/local/share/scalaVersionNumber`  

#####Spark (1.6 for Hadoop 2.6)
Similar installation instructions as for scala. [Download spark binaries](http://spark.apache.org/downloads.html), unpack them, and then move them somewhere safe.  
`tar -xzf spark-VN`  
Now move it somewhere safe.  
`mv spark-VN /usr/local/share/spark-VN`  

#####Updating system variables and path.
Open up  `~/.bashrc` and add the following lines to the end to add `_HOME` variables as environment variables and to put the binaries on the path
````
export JAVA_HOME=/usr/lib/jvm/java-8-oracle
export SCALA_HOME=/usr/local/share/scalaVersionNumber
export SPARK_HOME=/usr/local/share/sparkversionNumber

export PATH="$PATH:$SCALA_HOME/bin:$SPARK_HOME/bin" 
````
Restart the shell or `source ~./bash_rc`.

You should be up and running now. Since everyting was added to the path you can run them using their name (java, scala, spark-*). Eg., can just run `spark-shell` to access the shell. *There are some errors... Next step: GET RID OF THEM *

