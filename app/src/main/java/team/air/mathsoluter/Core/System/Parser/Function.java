package team.air.mathsoluter.Core.System.Parser;

import android.os.Environment;

import java.util.ArrayList;

public class Function implements FunctionListener {
    final Statement.FunctionStatement declaration;
    final Enviroment enviroment;
    public Function(Statement.FunctionStatement declaration)
    {
        this.declaration = declaration;
        enviroment=null;
    }

    public Function(Statement.FunctionStatement declaration, Enviroment enviroment)
    {
        this.declaration = declaration;
        this.enviroment=enviroment;
    }


    @Override
    public Object call(Enviroment enviroment, ArrayList<Object> arguments) {

        Enviroment env = null;
        if(this.enviroment==null)
            env=new Enviroment(enviroment);
        else env=new Enviroment(this.enviroment);

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