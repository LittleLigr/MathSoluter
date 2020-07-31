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

      
        return null;
    }

    @Override
    public int arg() {
        return Integer.MAX_VALUE;
    }
}
