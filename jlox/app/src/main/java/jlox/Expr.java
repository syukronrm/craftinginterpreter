package jlox;

abstract public class Expr {
    interface Visitor<R> {
        R visitBinaryExpr(Binary expr);
        R visitUnaryExpr(Unary expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
    }

    static class Binary extends Expr {
        final Expr expr1;
        final Token operator;
        final Expr expr2;

        Binary(Expr expr1, Token operator, Expr expr2) {
            this.expr1 = expr1;
            this.operator = operator;
            this.expr2 = expr2;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    static class Unary extends Expr {
        final Expr expr;
        final Token operator;

        Unary(Token operator, Expr expr) {
            this.operator = operator;
            this.expr = expr;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    static class Grouping extends Expr {
        final Expr expr;

        Grouping(Expr expr) {
            this.expr = expr;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    static class Literal extends Expr {
        final Object value;

        Literal(Object value) {
            this.value = value;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    abstract <R> R accept(Visitor<R> visitor);
}
