package jlox;

abstract class Stmt {
    interface Visitor<T> {
        T visitExpression(Expression expression);
        T visitPrint(Print print);
    }

    abstract <T> T accept(Visitor<T> visitor);

    static class Expression extends Stmt {
        Expr expression;

        Expression(Expr expression) {
            this.expression = expression;
        }

        <T> T accept(Visitor<T> visitor) {
            return visitor.visitExpression(this);
        }
    }

    static class Print extends Stmt {
        Expr expression;

        Print(Expr expression) {
            this.expression = expression;
        }

        <T> T accept(Visitor<T> visitor) {
            return visitor.visitPrint(this);
        }
    }
}
