package team.air.mathsoluter.Core.System.Parser;

import java.util.ArrayList;

public class UserExpressionFunction implements FunctionListener {

    final Statement.UserExpressionStatement declaration;
    public UserExpressionFunction(Statement.UserExpressionStatement declaration)
    {
        this.declaration = declaration;
    }

    @Override
    public Object call(Enviroment enviroment, ArrayList<Object> arguments) {

        Enviroment env = new Enviroment(enviroment);
        for (int i = 0; i < arguments.size(); i++)
        {
            Expression.Assign assign = ((Expression.Assign)arguments.get(i));
            Statement.VarStatement varStatement= new Statement.VarStatement(assign.name, assign.expression);
            varStatement.doAction(env);
        }

        try
        {
            declaration.body.doAction(env);
        }
        catch (Return value)
        {
            return value.value;
        }

        return null;
    }

    @Override
    public int arg() {
        return Integer.MAX_VALUE;
    }
}
