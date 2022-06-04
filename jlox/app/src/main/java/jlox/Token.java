package jlox;

import static jlox.TokenType.EOF;
import static jlox.TokenType.NUMBER;

public class Token {
    final TokenType type;

    // lexeme is literal string from the source text.
    final String lexeme;

    final Object literal;

    final int line;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal + " " + line;
    }

    @Override
    public boolean equals(Object token) {
        if (token instanceof Token) {
            // parse the literal into double
            if (this.type == NUMBER && this.literal instanceof Double) {
                Token tok = ((Token) token);
                if (tok.type == NUMBER && tok.literal instanceof Double) {
                    Double num1 = (Double) tok.literal;
                    Double num2 = (Double) this.literal;

                    return tok.lexeme.equals(this.lexeme)
                            && tok.line == this.line
                            && num1.compareTo(num2) == 0;
                }
            }

            // do not compare literal value of EOF
            if (this.type == EOF) {
                Token tok = ((Token) token);
                if (tok.type == EOF) {
                    return tok.lexeme.equals(this.lexeme)
                            && tok.line == this.line;
                }
            }
        }

        return super.equals(token);
    }
}

