CURRENT_DIR=`pwd`

compile:
	javac -cp craftinginterpreter/jlox/ -d build/ craftinginterpreter/jlox/craftinginterpreter/lox/*.java

run: compile
	java -cp build/ craftinginterpreter.lox.Lox
