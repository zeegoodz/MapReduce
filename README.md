Compile WordCount.java and create a jar:

$ export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar  //This should work on onyx

$ bin/hadoop com.sun.tools.javac.Main WordCount.java

$ jar cf wc.jar WordCount*.class
