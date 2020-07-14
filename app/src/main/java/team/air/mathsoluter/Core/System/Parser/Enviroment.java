package team.air.mathsoluter.Core.System.Parser;

import java.util.HashMap;
import java.util.Map;

import team.air.mathsoluter.Core.System.Token;

public class Enviroment {

    final Enviroment master;

    private Map<String, Object> values = new HashMap<>();

    public Enviroment()
    {master=null;}


    public Enviroment(Enviroment enviroment)
    {this.master=enviroment;}


    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(Token name) {
        if(values.containsKey(name.lexeme))
            return values.get(name.lexeme);
        if (master != null) return master.get(name);
        throw new ParserError();
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
