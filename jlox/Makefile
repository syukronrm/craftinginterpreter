CURRENT_DIR=`pwd`

gen_ast:
	javac -cp app/src/main/java/ -d build/ app/src/main/java/tool/*.java \
		&& java -cp build tool.GenerateAst app/src/main/java/jlox/

compile:
	javac -cp app/src/main/java -d build/ app/src/main/java/jlox/*.java

run: compile
	java -cp build jlox.Lox

