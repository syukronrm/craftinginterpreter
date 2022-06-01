Examples:
```
https://www.craftinginterpreters.com/the-lox-language.html

// This is comment
/*
 * This is multiline
 * comment
 */

// Print to console
print "Hello, World!";

// Bolean
true;
false;

// Number
1234;   // An integer
12.34;  // A decical number

// String
"Hello, World!"

// Null value
nil;

// Arithmetic
add + me;
subtract - me;
multiply * me;
divide / me;

-negate;

// Comparison
less < than;
lessThan <= orEqual;
greater > than;
greaterThan >= orEqual;

1 == 2;             // false.
"cat" != "dog";     // true.

// Strongly typed, no implicit conversion
123 == "123";       // false.

// Statement ends with trailing semicolon

// Block statement
{
  print "Hello";
  print "World";
}

var hello = "World";
hello = "Hello";

var nilDefault;

// Control flow
if (condition) {
  print "yes";
} else {
  print "no";
}

var a = 1;
while (a < 10) {
  print a;
  a = a + 1;
}

for (var a = 1; a < 10; a = a + 1) {
  print a;
}

makeBreakfast(bacon, eggs, toast);

fun printSum(a, b) { 
  print a + b;
}

// Closures
fun addPair(a, b) {
  return a + b;
}

fun identity(a) {
  return a;
}

print identity(addPair)(1, 2); // Prints "3".

var testA = "testA";
{
  var testA = "testAinside";
}
print testA;

```

Grammar:
```
program         -> declaration* EOF ;
declaration     -> varDeclaration
                   | statement ;
varDeclaration  -> "var" IDENTIFIER ( "=" expression ) ;
statement       -> exprStmt
                   | blockStmt
                   | printStmt ;
blockStmt       -> "{" declaration* "}" ;
exprStmt        -> expression ";" ;
printStmt       -> "print" expression ";" ;
expression      -> assignment ;
assignment      -> IDENTIFIER "=" assignment;
                   | equality ;
equality        -> comparison ( ( "!=" | "==" ) comparison )* ;
comparison      -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term            -> factor ( ( "-" | "+" ) factor )* ;
factor          -> unary ( ( "/" | "*" ) unary )* ;
unary           -> ( "!" | "-" ) unary
                   | primary ;
primary         -> NUMBER | STRING | "true" | "false" | "nil"
                   | "(" expression ")" ;
```

REPL:
```
> var a = "yes"; a
yes

> "test"
test

> 100 == 100;
true

``