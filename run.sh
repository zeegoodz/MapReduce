#!/bin/bash

bin/hdfs dfs -rmr /output
bin/hadoop com.sun.tools.javac.Main InvertedIndex.java
jar cf index.jar InvertedIndex*.class
bin/hadoop jar index.jar InvertedIndex /input /output
