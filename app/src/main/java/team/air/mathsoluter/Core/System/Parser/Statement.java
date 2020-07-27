package team.air.mathsoluter.Core.System.Parser;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        TextView consoleOutput;
        public PrintStatement(Expression expression, TextView consoleOutput) {
            super(expression);
            this.consoleOutput=consoleOutput;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            Object value = expression.doAction(enviroment);
            System.out.println(value.toString());
            consoleOutput.setText(consoleOutput.getText()+"\n"+value);
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

    static class WhileStatement extends Statement
    {
        final Expression condition;
        final Statement body;
        public WhileStatement(Expression condition, Statement body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            while ((boolean)condition.doAction(enviroment)==true)
                body.doAction(enviroment);
            return null;
        }
    }

    static class IfStatement extends Statement
    {
        final Expression condition;
        final Statement thenBranch, elseBranch;
        public IfStatement(Expression condition,  Statement thenBranch, Statement elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            Object cond = condition.doAction(enviroment);
           if(cond!=null&&(boolean)cond==true)
               thenBranch.doAction(enviroment);
           else if(elseBranch!=null)
               elseBranch.doAction(enviroment);
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

    static class FunctionStatement extends Statement
    {
        final ArrayList<Token> arguments;
        final Token name;
        final BlockStatement body;

        public FunctionStatement(Token name, ArrayList<Token> arguments, ArrayList<Statement> body) {
            this.arguments = arguments;
            this.body = new BlockStatement(body);
            this.name = name;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            enviroment.define(name.lexeme, new Function(this));

            return null;
        }
    }

    static class ClassStatement extends Statement
    {
        final ArrayList<FunctionStatement> functionStatements;
        final Token name;

        public ClassStatement(Token name, ArrayList<FunctionStatement> functionStatements) {
            this.functionStatements = functionStatements;
            this.name = name;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            Map<String, Function> methods = new HashMap<>();
            for (Statement.FunctionStatement method : functionStatements) {
                Function function = new Function(method, enviroment);
                methods.put(method.name.lexeme, function);
            }
            enviroment.define(name.lexeme, new Class(name.lexeme, methods));
            return null;
        }
    }


    static class UserExpressionStatement extends Statement
    {
        final Token name;
        final  Statement body;
        public UserExpressionStatement(Token name, Statement body) {
          this.body = body;
          this.name = name;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            enviroment.define(name.lexeme, new UserExpressionFunction(this));
            return null;
        }
    }

    static class ReturnStatement extends Statement
    {
      final Expression value;

        public ReturnStatement(Expression value) {
            this.value = value;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            if(value!=null)
                throw new Return(value.doAction(enviroment));
            return null;
        }
    }

    static class MathStatement extends Statement
    {
        final ArrayList<Statement> statements;

        public MathStatement(ArrayList<Statement> statements) {
            this.statements = statements;
        }

        @Override
        public Object doAction(Enviroment enviroment) {
            return null;
        }
    }

}
