package jlox;

import java.util.List;

public class AstPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    String print(Stmt statement) {
        return statement.accept(this);
    }

    String print(List<Stmt> statements) {
        StringBuilder string = new StringBuilder();

        for (Stmt stmt: statements) {
            string.append(stmt.accept(this));
            string.append("\n");
        }

        return string.toString();
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return null;
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return null;
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        return expr.value.toString();
    }

    @Override
    public String visitVarStmt(Stmt.Var expr) {
        return parenthesize("var " + expr.name + " = ", expr.initializer);
    }

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        return null;
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression expression) {
        return parenthesize("", expression.expression);
    }

    @Override
    public String visitPrintStmt(Stmt.Print print) {
        return parenthesize("print", print.expression);
    }

    private String parenthesize(String name, Expr... expressions) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr: expressions) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
