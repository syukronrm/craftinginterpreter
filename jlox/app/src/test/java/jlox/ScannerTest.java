package jlox;

import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static jlox.TokenType.*;

public class ScannerTest {
    @Test
    public void testNumber() {
        List<Token> tokens = this.scan("123.11 123");
        assertEquals(3, tokens.size());
    }

    @Test
    public void testString() {
        // Multiline string
        List<Token> tokens = this.scan("\"Hello\nEverynyan!\"");
        assertEquals(2, tokens.size());

        Token string = tokens.get(0);
        assertEquals("Hello\nEverynyan!", string.literal);
        assertEquals(STRING, string.type);

        Token eof = tokens.get(1);
        assertEquals(2, eof.line);
    }

    @Test
    public void testSingleLineComment() {
        List<Token> tokens = this.scan("// Hello everynyan!\n");
        assertEquals(1, tokens.size());

        Token eof = tokens.get(0);
        assertEquals(2, eof.line);
    }

    @Test
    public void testMultilineComment() {
        List<Token> tokens = this.scan("/* Hello\n * Everynyan!\n */");
        assertEquals(1, tokens.size());

        Token eof = tokens.get(0);
        assertEquals(3, eof.line);
    }

    @Test
    public void testScanner() {
        List<Token> tokens = this.scan("(123)");
        assertEquals(4, tokens.size());

        List<Token> tokens1 = this.scan("1 + yes + _ + 12");
        assertEquals(8, tokens1.size());
    }

    private List<Token> scan(String source) {
        Scanner s = new Scanner(source);
        List<Token> tokens = s.scanTokens();

        return tokens;
    }
}
