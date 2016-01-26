#### Get IntelliJ IDEA running with SPARK

This assumes you have sbt installed and on your path.  

-  Set up a directory with the usual structure for a spark scala project
````
mkdir SampleSpark
cd SampleSpark
mkdir -p src/{main,test}/{java,resources,scala}
````
- Add a build.sbt file to the parent directory. See [sampleBuild.sbt](https://github.com/stevencarlislewalker/sparkLearn/blob/master/snippets/buildTools/sampleBuild.sbt) for an example of a build file that specifies spark-core, spark-mllib, spark-csv dependencies. Pretty much all we need for what we are working on. The customary name for the file is build.sbt but (I think) as long as there is only one .sbt file it doesn't matter.

- `sbt compile`. This adds all the dependencies. 

- Open Intellij IDEA. File -> Open. Navigate to the directory containing the project (in this case SampleApp) and open.

- For Windows Only. Even though we are running this on a local machine Spark tries to talk with Hadoop which can be an issue on Windows. The workaround is to download [winutils.exe](http://social.msdn.microsoft.com/Forums/windowsazure/en-US/28a57efb-082b-424b-8d9e-731b1fe135de/please-read-if-experiencing-job-failures?forum=hdinsight) and put `winutils.exe` in some folder `filepath/bin`. Then in your Scala main method add the line
```` System.setProperty("hadoop.home.dir", "filepath") ````

- Construct a new Scala Class and run it. See [sampleApp]() for an example. This should also clarify the previous step.