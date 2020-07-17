package team.air.mathsoluter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import team.air.mathsoluter.Core.System.Lexer.Lexer;
import team.air.mathsoluter.Core.System.Parser.Expression;
import team.air.mathsoluter.Core.System.Parser.Interpretator;
import team.air.mathsoluter.Core.System.Parser.Parser;
import team.air.mathsoluter.Core.System.Token;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView v = (TextView)findViewById(R.id.editText);
        v.setText("function name(a)" +
                "{" +
                "print a;" +
                "}" +
                "name(\"iva\");");
    }

    public void click(View view)
    {
        TextView v = (TextView)findViewById(R.id.editText);
        ArrayList<Token> tokens = new Lexer().lex(v.getText().toString());
        new Interpretator().interpret(new Parser(tokens).parse());
    }
}
