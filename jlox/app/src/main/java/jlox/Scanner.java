package jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jlox.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {
        this.source = source;
    }

    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and",     AND);
        keywords.put("class",   CLASS);
        keywords.put("else",    ELSE);
        keywords.put("false",   FALSE);
        keywords.put("fun",     FUN);
        keywords.put("for",     FOR);
        keywords.put("if",      IF);
        keywords.put("nil",     NIL);
        keywords.put("or",      OR);
        keywords.put("print",   PRINT);
        keywords.put("return",  RETURN);
        keywords.put("super",   SUPER);
        keywords.put("this",    THIS);
        keywords.put("true",    TRUE);
        keywords.put("var",     VAR);
        keywords.put("while",   WHILE);
    }

    public List<Token> scanTokens() {
        while(!isAtEnd()) {
            this.start = this.current;
            scanToken();
        }

        this.tokens.add(new Token(EOF, "", null, line));
        return this.tokens;
    }

    public void scanToken() {
        char c = this.advance();

        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '?': addToken(QUESTION); break;
            case ':': addToken(COLON); break;
            case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
            case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
            case '<': addToken(match('=') ? LESS_EQUAL : LESS); break;
            case '>': addToken(match('=') ? GREATER_EQUAL : GREATER); break;
            case '/':
                if (match('/')) {
                    // Comment
                    while (this.peek() != '\n' && !this.isAtEnd()) this.advance();
                } else if (match('*')) {
                    // Multiline comment
                    while (!(this.peek() == '*' && this.peekNext() == '/')) {
                        if (this.peek() == '\n') {
                            this.line++;
                        }
                        this.advance();
                    }

                    this.advance();
                    this.advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case '\n':
                this.line++;
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace
                break;
            case '"': this.string(); break;
            default:
                if (isDigit(c)) {
                    this.number();
                } else if (isAlpha(c)) {
                    this.identifier();
                } else {
                    Lox.error(line, "Unexpected character.");
                }
        }
    }

    private void identifier() {
        while (isAlpha(peek())) advance();

        String str = this.source.substring(start, current);

        TokenType token = keywords.get(str);
        if (token == null) {
            addToken(IDENTIFIER, str);
        }  else {
            addToken(token, str);
        }
    }

    private void number() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private void string() {
        while(peek() != '"' && !isAtEnd()) {
            char c = advance();
            if (c == '\n') this.line++;
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        advance();

        String value = this.source.substring(this.start + 1, this.current - 1);
        addToken(STRING, value);
    }

    private char advance() {
        return this.source.charAt(this.current++);
    }

    private void addToken(TokenType type) {
        this.addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String lexeme = this.source.substring(this.start, this.current);
        Token t = new Token(type, lexeme, literal, this.line);
        this.tokens.add(t);
    }

    private boolean match(char c) {
        if (isAtEnd()) return false;
        return this.source.charAt(this.current) == c;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\n';
        return source.charAt(current + 1);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
}
