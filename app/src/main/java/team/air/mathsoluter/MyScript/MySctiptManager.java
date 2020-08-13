package team.air.mathsoluter.MyScript;

import com.myscript.iink.Engine;


public class MySctiptManager {

    static Engine engine;
    public static synchronized Engine getEngine()
    {
        if (engine==null)
            engine = Engine.create(MyCertificate.getBytes());
        return engine;
    }
}
