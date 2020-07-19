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
import team.air.mathsoluter.Core.System.Parser.Statement;
import team.air.mathsoluter.Core.System.Token;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView v = (TextView)findViewById(R.id.editText);
        v.setText("print \\int_{1}^{2}{3};");
        System.out.println(v.getText());
    }

    public void click(View view)
    {
        TextView v = (TextView)findViewById(R.id.editText);
        String integ = "function INTEGRAL(down, top, value)" +
                "{" +
                "return down+top+value;" +
                "}";
        ArrayList<Token> tokens = new Lexer().lex(integ+v.getText().toString());
        //for(Token tok : tokens)
            //System.out.println(tok.type);
     //   ArrayList<Statement> tokens_pars = new Parser(tokens).parse();
        new Interpretator().interpret(new Parser(tokens).parse());
    }
}
