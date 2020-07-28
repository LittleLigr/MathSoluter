package team.air.mathsoluter.Core.System.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import team.air.mathsoluter.Core.System.Token;

public class Class implements FunctionListener {
    final String name;
    final Map<String,Function> functions;
    final int argCount;
    Class(String name,Map<String,Function> functions)
    {
        this.name = name;
        this.functions=functions;
        Function init = findMethod("init");
        if(init!=null)
            argCount = init.arg();
        else argCount=0;
    }

    Function findMethod(String name)
    {
        if(functions.containsKey(name))
            return functions.get(name);
        return null;
    }

    @Override
    public String toString() {
        return "ScriptClass{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public Object call(Enviroment enviroment, ArrayList<Object> arguments) {
        return new ClassInstance(this, arguments);
    }

    @Override
    public int arg() {
        return argCount;
    }
}

class ClassInstance
{
    private Class classDescription;
    private final Map<String, Object> fields = new HashMap<>();

    ClassInstance(Class classDescription, ArrayList<Object> arguments)
    {
        this.classDescription = classDescription;
        fields.put("this", this);
        Function init = classDescription.findMethod("init");
        if(init!=null)
                init.call(new Enviroment(fields), arguments);
        else throw new ParserError();
    }
    public Object get(Token name)
    {
        if(fields.containsKey(name.lexeme))
            return fields.get(name.lexeme);

        Function function = classDescription.findMethod(name.lexeme);
        if(function!=null)
            return new Function(function.declaration, new Enviroment(fields));

        throw new ParserError();
    }

    void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }
    @Override
    public String toString() {
        return classDescription.toString();
    }
}
