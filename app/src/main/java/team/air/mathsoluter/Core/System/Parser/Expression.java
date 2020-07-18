package team.air.mathsoluter.Core.System.Parser;

import android.widget.Switch;

import java.util.ArrayList;

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

    static class Call extends Expression{

        final Expression callee;
        final ArrayList<Expression> arguments;

        public Call(Expression callee, ArrayList<Expression> arguments)
        {
            this.callee = callee;
            this.arguments = arguments;
        }

        @Override
        public String toString() {
            return callee.toString();
        }


        @Override
        public boolean isNotNull() {
            if(callee!=null && arguments!=null)
                return true;
            return false;
        }
        @Override
        public Object doAction(Enviroment enviroment) {
            Object calleeResult = callee.doAction(enviroment);

            if(!(calleeResult instanceof FunctionListener))
                throw new ParserError();

            FunctionListener functionStatement = (FunctionListener)calleeResult;

            if(arguments.size() != functionStatement.arg())
                throw new ParserError();

            ArrayList<Object> arguments = new ArrayList<>();
            for (Expression arg : this.arguments)
                arguments.add(arg.doAction(enviroment));

            return functionStatement.call(enviroment, arguments);
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
            enviroment.assign(name, value);
            return value;
        }
    }

    static class MathExpression2Arg extends Expression{

        final Expression expression1, expression2;
        final Token.TokenType type;

        public MathExpression2Arg(Token.TokenType type, Expression expression1, Expression expression2)
        {
            this.type = type;
            this.expression1 = expression1;
            this.expression2 = expression2;
        }

        @Override
        public boolean isNotNull() {
            if(type!=null && expression1!=null&& expression2!=null)
                return true;
            return false;
        }

        @Override
        public String toString() {
            return type.toString()+" ("+expression1.toString()+","+expression2.toString()+")";
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            if(type== Token.TokenType.FRAC)
                return (new Binary(expression1, new Token(Token.TokenType.SLASH), expression2).doAction(enviroment));
            return null;
        }
    }

    static class MathExpression1Arg extends Expression{

        final Expression expression;
        final Token.TokenType type;

        public MathExpression1Arg(Token.TokenType type, Expression expression)
        {
            this.type = type;
            this.expression = expression;
        }

        @Override
        public boolean isNotNull() {
            if(type!=null && expression!=null)
                return true;
            return false;
        }

        @Override
        public String toString() {
            return type.toString()+" (" +expression.toString()+")";
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            switch(type)
            {
                case SIN:
                    return Math.sin((double)expression.doAction(enviroment));
                case COS:
                    return Math.cos((double)expression.doAction(enviroment));
                case TAN:
                    return Math.tan((double)expression.doAction(enviroment));
                case EXP:
                    return Math.exp((double)expression.doAction(enviroment));
                case SQRT:
                    return Math.sqrt((double)expression.doAction(enviroment));
                case LOG:
                    return Math.log((double)expression.doAction(enviroment));
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "empty expression";
    }
}
