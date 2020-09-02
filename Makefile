CURRENT_DIR=`pwd`

compile:
	javac -cp craftinginterpreter/jlox/ -d build/ craftinginterpreter/jlox/craftinginterpreter/lox/*.java

run: compile
	java -cp build/ craftinginterpreter.lox.Lox

run_gen_ast:
	javac -cp craftinginterpreter/jlox/ -d build/ craftinginterpreter/jlox/craftinginterpreter/tool/*.java
	java -cp build craftinginterpreter.tool.GenerateAst craftinginterpreter/jlox/craftinginterpreter/lox
