#Port numbers:
Zach: 40800 or 43100

Kyle: 40500 or 42800

#Misc before running scripts:
  $ export PATH=~/hadoop-install/hadoop/bin
  
  $ export PATH=~/hadoop-install/hadoop/sbin
  
#Run scripts:
  1. $ ./setmode.sh distributed
  2. $ ./cluster-pickports.sh [your ports]
  3. $ Open up another session tab, pbsget nodes
  4. $ ./create-hadoop-cluster.sh

#To check hdfs status through command line:
  $ hdfs dfsadmin -report *make sure configured capacity is not 0gb.
  
#Making and running jars

  Compile WordCount.java and create a jar:

  $ export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar  //This should work on onyx

  $ bin/hadoop com.sun.tools.javac.Main WordCount.java  

  $ jar cf wc.jar WordCount*.class
  
  $ hadoop jar [path to jar file] [main class] [args]
  
  Example: $ hadoop jar InvertedIndex.jar InvertedIndex input output
  
#After done running 

  1. Save ouptut file from the HDFS because it will be erased when the file system is shut down
  2. $ cluster-remove.sh
  
