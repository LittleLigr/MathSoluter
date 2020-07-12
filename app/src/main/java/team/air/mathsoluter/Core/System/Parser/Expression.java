package team.air.mathsoluter.Core.System.Parser;

import android.widget.Switch;

import team.air.mathsoluter.Core.System.Token;

public class Expression implements ActionListener {

    public boolean isBoolean(Object value)
    {
        if(value instanceof Boolean)
            return true;
        return false;
    }

    public boolean isNumerical(Object value)
    {
        if(value instanceof Double)
            return true;
        return false;
    }

    public boolean isString(Object value)
    {
        if(value instanceof String)
            return true;
        return false;
    }

    public boolean isNotNull()
    {
        return true;
    }

    @Override
    public Object doAction(Enviroment enviroment) {
        return null;
    }

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

        @Override
        public boolean isNotNull() {
            if(left!=null && right!=null && type!=null)
                return true;
            return false;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            Object right = this.right.doAction(enviroment);
            Object left = this.left.doAction(enviroment);
            switch(type.type){
                case PLUS: {
                    if (isNumerical(left))
                    {
                        if (isNumerical(right))
                            return (double) left + (double) right;
                    }
                    else if(isString(left))
                        return left.toString()+right.toString();
                    else if(isBoolean(left))
                        if(isBoolean(right))
                        {
                            if((boolean)left==true||(boolean)right==true)
                                return true;
                        }
                }
                case MINUS:
                {
                    if (isNumerical(left))
                    {
                        if (isNumerical(right))
                            return (double) left - (double) right;
                    }
                    else if(isString(left))
                    {
                        if(isString(right))
                            return left.toString().replace(right.toString(), "");
                        else if(isNumerical(right))
                            return left.toString().substring(0, left.toString().length()-((Double)right).intValue());
                    }
                    else if(isBoolean(left))
                    {
                        if(isBoolean(right))
                            return false;
                    }
                }
                case STAR:
                {
                    if (isNumerical(left))
                    {
                        if (isNumerical(right))
                            return (double) left * (double) right;
                    }
                    else if(isString(left))
                    {
                        if(isNumerical(right))
                        {
                            String res = "";
                            if(isNumerical(right))
                                for(int i = 0 ; i < (double)right; i++)
                                    res+=left.toString();
                            return res;
                        }
                    }
                    else if(isBoolean(left))
                    {
                        if(isBoolean(right))
                            if((boolean)left==true&&(boolean)right==true)
                                return true;
                            else return false;

                    }
                }
                case SLASH:
                {
                    if (isNumerical(left))
                        if (isNumerical(right))
                            return (double) left / (double) right;
                }
                case EQUAL_EQUAL:
                {
                    return left.equals(right);
                }
                case BANG_EQUAL:
                {
                    return !left.equals(right);
                }
                case LESS:
                {
                    if(isNumerical(left))
                        if(isNumerical(right))
                            return (double)left<(double)right;
                    if(isString(left))
                        if(isString(right))
                            return left.toString().length()<right.toString().length();
                }
                case LESS_EQUAL:
                {
                    if(isNumerical(left))
                        if(isNumerical(right))
                            return (double)left<=(double)right;
                    if(isString(left))
                        if(isString(right))
                            return left.toString().length()<=right.toString().length();
                }
                case GREATER_EQUAL:
                {
                    if(isNumerical(left))
                        if(isNumerical(right))
                            return (double)left>=(double)right;
                    if(isString(left))
                        if(isString(right))
                            return left.toString().length()>=right.toString().length();
                }
                case GREATER:{
                    if(isNumerical(left))
                        if(isNumerical(right))
                            return (double)left>(double)right;
                    if(isString(left))
                        if(isString(right))
                            return left.toString().length()>right.toString().length();
                }

            }
            throw new ParserError();
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
        @Override
        public boolean isNotNull() {
            if(token!=null && expression!=null)
                return true;
            return false;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            return expression.doAction(enviroment);
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


        @Override
        public boolean isNotNull() {
            if(value!=null )
                return true;
            return false;
        }
        @Override
        public Object doAction(Enviroment enviroment) {
            return value;
        }
    }

    static class Variable extends Expression{

        Token value;

        public Variable(Token value)
        {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }


        @Override
        public boolean isNotNull() {
            if(value!=null )
                return true;
            return false;
        }

        @Override
        public Object doAction( Enviroment enviroment) {
            return enviroment.get(value);
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


        @Override
        public boolean isNotNull() {
            if(expression!=null)
                return true;
            return false;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            return expression.doAction(enviroment);
        }
    }

    static class Assign extends Expression{

        final Expression expression;
        final Token name;

        public Assign(Token name, Expression expression)
        {
            this.name = name;
            this.expression = expression;
        }

        @Override
        public boolean isNotNull() {
            if(name!=null && expression!=null)
                return true;
            return false;
        }

        @Override
        public String toString() {
            return expression.toString();
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            Object value = expression.doAction(enviroment);
            enviroment.assign(name, expression);
            return value;
        }
    }

    @Override
    public String toString() {
        return "empty expression";
    }
}
