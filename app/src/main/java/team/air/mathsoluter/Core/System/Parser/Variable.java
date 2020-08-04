package team.air.mathsoluter.Core.System.Parser;

import java.util.ArrayList;

import team.air.mathsoluter.Core.System.Token;

public class Variable {

    public Object value=null;
    public Statement.BlockStatement setter, getter;

    public Variable(Object value, Statement.BlockStatement setter, Statement.BlockStatement getter)
    {
        this.value = value;
        this.setter = setter;
        this.getter = getter;
    }

    public void set(Object value)
    {
        if(setter!=null)
        {
            Enviroment enviroment = new Enviroment();
            enviroment.define("input", value);
            enviroment.define("value", this.value);
            setter.doAction(enviroment);
            this.value = enviroment.get(new Token(Token.TokenType.NULL, "value",null,0));
        }
        else this.value = value;
    }

    public Object get()
    {
        if(getter!=null)
        {
            Enviroment enviroment = new Enviroment();
            enviroment.define("value", value);

            try
            {
                getter.doAction(enviroment);
            }
            catch (Return value)
            {
                return value.value;
            }
            return null;
        }

        return value;
    }

}
