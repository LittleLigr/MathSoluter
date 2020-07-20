package team.air.mathsoluter.Core.System.Parser;

import java.util.ArrayList;

public interface FunctionListener {
    Object call(Enviroment enviroment, ArrayList<Object> arguments);
    int arg();
}
