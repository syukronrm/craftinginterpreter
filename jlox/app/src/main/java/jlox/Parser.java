package jlox;

import java.util.ArrayList;
import java.util.List;
import static jlox.TokenType.*;

public class Parser {
    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;
    private boolean isRepl = false;
    private boolean isPrintLastStatement = false;

    Parser(List<Token> tokens, boolean isRepl) {
        this.isRepl = isRepl;
        this.tokens = tokens;
    }

    Parser(List<Token> tokens) {
        this(tokens, false);
    }

    public boolean isPrintLastStatement() {
        return isPrintLastStatement;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!match(EOF)) {
            statements.add(declaration());
        }
        return statements;
    }

    private Stmt declaration() {
        try {
            if (match(VAR)) {
                return varDeclaration();
            }

            return statement();
        } catch (ParseError error) {
            return null;
        }
    }

    private Stmt varDeclaration() {
        Token name = consume(IDENTIFIER, "Expect variable name.");

        Expr init = null;
        if (match(EQUAL)) {
            init = expression();
        }

        consume(SEMICOLON, "Expect ';' after declaration.");
        return new Stmt.Var(name, init);
    }

    private Stmt ifStatement() {
        consume(LEFT_PAREN, "Expect `(` left parenthesis");
        Expr expr = expression();
        consume(RIGHT_PAREN, "Expect `)` right parenthesis");
        Stmt thenStmt = statement();

        Stmt elseStmt = null;
        if (match(ELSE)) {
            elseStmt = statement();
        }
        return new Stmt.If(expr, thenStmt, elseStmt);
    }

    private Stmt statement() {
        if (match(PRINT)) {
            Expr expr = expression();
            consume(SEMICOLON, "Expecting semicolon.");
            return new Stmt.Print(expr);
        }

        if (match(IF)) {
            return ifStatement();
        }

        if (match(WHILE)) {
            return whileStatement();
        }

        if (match(FOR)) {
            return forStatement();
        }

        if (match(LEFT_BRACE)) return new Stmt.Block(block());

        Expr expr = expression();
        if (isRepl && isAtEnd()) {
            isPrintLastStatement = true;
            advance();
        } else {
            consume(SEMICOLON, "Expecting semicolon.");
        }
        return new Stmt.Expression(expr);
    }

    private Stmt forStatement() {
        consume(LEFT_PAREN, "Expecting `(` for `for` loop");

        Stmt initializer = null;
        if (peek().type == SEMICOLON) {
            consume(SEMICOLON, "Expect first `;` for empty initializer in `for`");
        } else if (match(VAR)) {
            initializer = varDeclaration();
        } else {
            initializer = expressionStatement();
        }

        Expr condition = null;
        if (peek().type != SEMICOLON) {
            condition = expression();
        }
        consume(SEMICOLON, "Expect second `;` after condition expression in `for`");

        Expr advancement = null;
        if (peek().type != RIGHT_PAREN) {
            advancement = expression();
        }
        consume(RIGHT_PAREN, "Expecting `)` for `for` loop");
        Stmt body = statement();

        return new Stmt.For(initializer, condition, advancement, body);
    }

    private Stmt whileStatement() {
        consume(LEFT_PAREN, "Expect `(` left parenthesis for `while`");
        Expr condition = expression();
        consume(RIGHT_PAREN, "Expect `)` right parenthesis for `while`");
        Stmt body = statement();

        return new Stmt.While(condition, body);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();

        while(!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(RIGHT_BRACE, "Expect '}' after block;");
        return statements;
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(SEMICOLON, "Expect `;` after an expression");
        return new Stmt.Expression(expr);
    }

    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {
        Expr expr = logicOr();

        if (match(EQUAL)) {
            Token equals = previous();
            Expr value = assignment();

            if (expr instanceof Expr.Variable) {
                Token name = ((Expr.Variable)expr).name;
                return new Expr.Assign(name, value);
            }

            error(equals, "Invalid assignment target.");
        }

        return expr;
    }

    private Expr logicOr() {
        Expr left = logicAnd();

        if (match(OR)) {
            Token token = previous();
            Expr right = logicAnd();
            return new Expr.Logical(left, token, right);
        }

        return left;
    }

    private Expr logicAnd() {
        Expr left = equality();

        if (match(AND)) {
            Token token = previous();
            Expr right = equality();
            return new Expr.Logical(left, token, right);
        }

        return left;
    }

    private Expr equality() {
        Expr expr = comparison();

        while (match(BANG_EQUAL, BANG_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr comparison() {
        Expr expr = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();

            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while (match(PLUS, MINUS)) {
            Token operator = previous();
            Expr right = factor();

            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() {
        Expr expr = unary();

        while (match(STAR, SLASH)) {
            Token operator = previous();
            Expr right = unary();

            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = primary();

            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    private Expr primary() {
        if (match(STRING)) return new Expr.Literal(previous().literal);
        if (match(NUMBER)) return new Expr.Literal(previous().literal);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(NIL)) return new Expr.Literal(null);

        if (match(IDENTIFIER)) return new Expr.Variable(previous());

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    private Token consume(TokenType token, String message) {
        if (check(token)) return advance();

        throw error(peek(), message);
    }

    private boolean match(TokenType... types) {
        Token t = this.tokens.get(this.current);
        for (TokenType type : types) {
            if (t.type == type) {
                advance();
                return true;
            }
        }
        return false;
    }

    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return this.tokens.get(this.current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
}
