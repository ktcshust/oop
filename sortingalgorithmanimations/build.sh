#!/bin/sh
set -e
[ "$1" = "-v" ] && set -x

cd $(dirname "$0")
SRC=$(find ./src -name *.java)
BUILDDIR="./build"
MAINCLASS="net.bohush.sorting.Main"

echo "[$(date +%T)] Building"
javac -d $BUILDDIR --release 17 $SRC

classfiles=$(find $BUILDDIR -name *.class | cut -d/ -f2-)
echo "[$(date +%T)] Creating JAR archive" 
jar -c -e $MAINCLASS -f sorting.jar -C $BUILDDIR .
