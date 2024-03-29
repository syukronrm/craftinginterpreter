package jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;
    static boolean hadEOF = false;

    static boolean isRepl = false;

    // TODO: add debug mode to print the AST before evaluating.
    private boolean debugMode = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            isRepl = true;
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        while (!hadEOF) {
            System.out.print("> ");
            Object val = run(reader.readLine());
            hadError = false;

            if (val != null) {
                System.out.println(val);
            }
        }
    }

    private static Object run(String source) {
        if (source == null) {
            hadEOF = true;
            return null;
        }

        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens, isRepl);
        List<Stmt> statements = parser.parse();

        if (hadError) return null;
        interpreter.interpret(statements);

        if (parser.isPrintLastStatement()) {
            return interpreter.getLastExpressionValue();
        }

        return null;
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    static void runtimeError(RuntimeError error) {
        hadRuntimeError = true;
        System.out.println(error.getMessage() + "\n[line " + error.token.line + "]");
    }

    private static void report(int line, String where, String message) {
        System.out.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
