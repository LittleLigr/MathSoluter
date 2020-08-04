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

class MathClass implements ClassListener
{
    final Map<String,FunctionListener> functions = new HashMap<>();
    final Map<String, Object> fields = new HashMap<>();
    final Expression expression;
    MathClass(final Expression expression) {
        this.expression = expression;
        functions.put("setVar", new FunctionListener() {
            @Override
            public Object call(Enviroment enviroment, ArrayList<Object> arguments) {
                for(int i = 0; i < arguments.size(); i+=2)
                    fields.put((String) arguments.get(i), arguments.get(i+1));
                return null;
            }

            @Override
            public int arg() {
                return 2;
            }
        });
        functions.put("solve", new FunctionListener() {
            @Override
            public Object call(Enviroment enviroment, ArrayList<Object> arguments) {
                Enviroment env = new Enviroment(enviroment, fields);
                return expression.doAction(env);
            }

            @Override
            public int arg() {
                return 0;
            }
        });
    }

    public Object get(Token name)
    {
        if(fields.containsKey(name.lexeme))
            return fields.get(name.lexeme);

        FunctionListener function = findMethod(name.lexeme);
        if(function!=null)
            return function;

        throw new ParserError();
    }

    FunctionListener findMethod(String name)
    {
        if(functions.containsKey(name))
            return functions.get(name);
        return null;
    }

    public void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }
}

class ClassInstance implements ClassListener
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

    public void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }
    @Override
    public String toString() {
        return classDescription.toString();
    }
}
interface ClassListener
{
    public void set(Token name, Object value);
    public Object get(Token name);
}