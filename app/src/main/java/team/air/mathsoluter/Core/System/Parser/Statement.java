package team.air.mathsoluter.Core.System.Parser;

import java.util.ArrayList;

import team.air.mathsoluter.Core.System.Token;

public class Statement implements ActionListener{
    final Expression expression;

    public Statement(Expression expression)
    {
        this.expression = expression;
    }

    public Statement(){
        expression=null;
    }

    @Override
    public Object doAction(Enviroment enviroment) {
        return null;
    }

    static class PrintStatement extends Statement
    {

        public PrintStatement(Expression expression) {
            super(expression);
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            Object value = expression.doAction(enviroment);
            System.out.println(value.toString());
            return null;
        }
    }

    static class ExpressionStatement extends Statement
    {

        public ExpressionStatement(Expression expression) {
            super(expression);
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            expression.doAction(enviroment);
            return null;
        }
    }

    static class VarStatement extends Statement
    {
        final Token name;
        public VarStatement(Token name, Expression expression) {
            super(expression);
            this.name = name;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            Object value = null;
            if(expression!=null)
                value = expression.doAction(enviroment);
            enviroment.define(name.lexeme, value);
            return null;
        }
    }

    static class BlockStatement extends Statement
    {
        final ArrayList<Statement> statements;
        public BlockStatement(ArrayList<Statement> statements) {
            this.statements = statements;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
           Enviroment enviromentChild = new Enviroment(enviroment);

           for(Statement statement : statements)
               statement.doAction(enviromentChild);
            return null;
        }
    }

}
