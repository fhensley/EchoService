#!/bin/bash

CP='';

for i in `ls *.jar`; do
        CP="$CP:/tmp/$i";
done
echo $CP
export JAVA_HOME=/usr/lib/jvm/java-1.7.0-openjdk-amd64
$JAVA_HOME/bin/java -cp $CP Main $1 $2 $3