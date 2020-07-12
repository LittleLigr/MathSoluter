package team.air.mathsoluter.Core.System.Parser;

import java.util.ArrayList;

public class Interpretator {

    Enviroment enviroment = new Enviroment();

    public void interpret(ArrayList<Statement> statements)
    {
        try {
            for (Statement statement : statements) {
                statement.doAction(enviroment);
            }
        } catch (ParserError error) {
            System.out.println("interpretate error");
        }
    }


}
