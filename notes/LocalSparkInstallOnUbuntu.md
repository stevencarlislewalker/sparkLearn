*Jan 2016*

Up and running with local instance of spark on ubuntu 15.10. Installing Java, Scala, and Spark. For a local instance of Spark there is no need to install Scala - Spark (prebuilt) comes with Scala (2.10.5). I don't know if you still need to install Java - probably not.

####Instructions - installing java, scala, and spark on Ubuntu-15.10-desktop-amd64

#####Gotchas (errors Ben made):
- Moving files requires sudo permissions. Need to change to root for those commands (`sudo -i`). Don't forget to change back right after (`su "username"`) since you don't want to edit root `PATH` and environment variables.
- When editing configuration files (~/.profile, etc/environment, etc (verbatim etc this time)) in order for your session to 'see' the changes just run `source filenameThatWasChanged`. 

#####Java (1.8.0_66) 
Note: non- Oracle version 'may' have issues with spark. Play it safe and download the Oracle version.
`sudo add-apt-repository ppa:webupd8team/java`
`sudo apt-get update`
`sudo apt-get install oracle-java8-installer`

Add JAVA_HOME to environment variables
`sudo echo "JAVA_HOME=/usr/lib/jvm/java-8-oracle" >> /etc/environment`


#####Scala (2.11.7)
Download from [scala download page](http://www.scala-lang.org/download/install.html) and unpack. Then move somewhere safe - '/usr/local/share/' seems to be the default location for this kind of thing.
`tar -xzf scalaVersionNumber.tar.gz`
`mv scalaVersionNumber /usr/local/share/scalaVersionNumber`

set SCALA_HOME variable in etc/environment 
`echo "SCALA_HOME=/usr/local/share/scalaVersionNumber" >> /etc/environment`

Add scala bin to the path. Best place to put this is ~/.profile or ~/.bash_profile. 
Still not clear on which one but note that if ~/.bash_profile exists bash won't read ~/.profile so I can never ever have a bash_profile.
`echo "$PATH=$PATH:$SCALA_HOME/bin" >> ~/.profile`

#####Spark (1.6 for Hadoop 2.6)
Similar installation instructions as for scala. [Download spark binaries](http://spark.apache.org/downloads.html), unpack them, and then move them somewhere safe. 
`tar -xzf spark-VN`
Now move it somewhere safe. 
`mv spark-VN /usr/local/share/spark-VN`

Set `SPARK_HOME` as environment variable
`echo "SPARK_HOME=/usr/local/share/sparkversionNumber" >> /etc/environment`

Add spark bin to the path. 
`echo "$PATH=$PATH:$SPARK_HOME/bin" >> ~/.profile`
Check out what's inside the bin file. 
`ls $SPARK_HOME/bin` 
Lots of good stuff including spark-shell and spark-submit. That's mostly what we'll be using.

You should be up and running now. Since you added Spark's bin to the path can just run `spark-shell` to access the shell. *There are some errors... Next step: GET RID OF THEM*

