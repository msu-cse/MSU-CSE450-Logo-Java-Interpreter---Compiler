java -jar lib/jasmin.jar -d build/src/assembly $*
class=$(basename $*)
java -cp lib/turtle.jar:build/src/assembly ${class%.*}