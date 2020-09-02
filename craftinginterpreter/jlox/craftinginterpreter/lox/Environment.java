package craftinginterpreter.lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, Object> values = new HashMap<>();
    private Environment enclosing;

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    void define(String name, Object value) {
        values.put(name, value);
    }

    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (enclosing != null) {
            enclosing.get(name);
        }

        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }
}
