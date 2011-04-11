java -jar lib/jasmin.jar -d build/src/assembly $*
class=$(basename $*)
java -cp build/src/assembly ${class%.*}