package team.air.mathsoluter.Core.System.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team.air.mathsoluter.Core.System.Token;

public class Enviroment {

    final Enviroment master;
    static Enviroment global = new Enviroment();
    static {
        global.define("system_clock", new FunctionListener() {
            @Override
            public Object call(Enviroment enviroment, ArrayList<Object> arguments) {
                return (double)System.currentTimeMillis();
            }

            @Override
            public int arg() {
                return 0;
            }
        });
    }

    private Map<String, Object> values = new HashMap<>();

    public Enviroment()
    {master=null;}


    public Enviroment(Enviroment enviroment)
    {this.master=enviroment;}

    public Enviroment(Map <String, Object> values)
    {this.values = values;
    master=null;}

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(Token name) {
        if(values.containsKey(name.lexeme))
            return values.get(name.lexeme);
        if (master != null) return master.get(name);
        else if(global.contain(name)) return global.get(name);
        throw new ParserError();
    }

    boolean contain(Token name)
    {
        if(values.containsKey(name.lexeme))
            return true;
        return false;
    }

    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        if (master != null) {
            master.assign(name, value);
            return;
        }

        throw new ParserError();
    }

}
