package team.air.mathsoluter.Core.System.Parser;

import team.air.mathsoluter.Core.System.Token;

public class Expression {


    static class Binary extends Expression{
        final Expression left;
        final Token type;
        final Expression right;

        public Binary(Expression left, Token type, Expression rigth)
        {
            this.left = left;
            this.right = rigth;
            this.type = type;
        }

        @Override
        public String toString() {
            return "("+left.toString()+" "+type.type.toString()+" "+right.toString()+")";
        }
    }

    static class Unary extends Expression{

        final Token token;
        final Expression expression;

        public Unary(Token token, Expression expression)
        {
            this.token = token;
            this.expression=expression;
        }

        @Override
        public String toString() {
            return token.literal.toString();
        }
    }

    static class Literal extends Expression{

        final Object value;

        public Literal(Object value)
        {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    static class Grouping extends Expression{

        final Expression expression;

        public Grouping(Expression expression)
        {
            this.expression = expression;
        }

        @Override
        public String toString() {
            return expression.toString();
        }
    }

    @Override
    public String toString() {
        return "empty expression";
    }
}
