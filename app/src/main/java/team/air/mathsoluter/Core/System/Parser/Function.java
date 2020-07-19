package team.air.mathsoluter.Core.System.Parser;

import java.util.ArrayList;

public class Function implements FunctionListener {
    final Statement.FunctionStatement declaration;
    public Function(Statement.FunctionStatement declaration)
    {
        this.declaration = declaration;
    }

    @Override
    public Object call(Enviroment enviroment, ArrayList<Object> arguments) {
        Enviroment env = new Enviroment(enviroment);
        for (int i = 0; i < declaration.arguments.size(); i++)
            env.define(declaration.arguments.get(i).lexeme,arguments.get(i));
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
        return declaration.arguments.size();
    }
}
