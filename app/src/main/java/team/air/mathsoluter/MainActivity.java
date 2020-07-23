package team.air.mathsoluter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import katex.hourglass.in.mathlib.MathView;
import team.air.mathsoluter.Core.System.Lexer.Lexer;
import team.air.mathsoluter.Core.System.Parser.Interpretator;
import team.air.mathsoluter.Core.System.Parser.Parser;
import team.air.mathsoluter.Core.System.Token;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView v = (TextView)findViewById(R.id.editText);


        v.setText("class Bagel{\n" +
                " call()" +
                "{\n" +
                "print this;\n" +
                "}\n" +
                "}" +
                "var bagel = Bagel().call;\n" +
                "print bagel();");
        //System.out.println(v.getText());
    }

    public void click(View view)
    {
        MathView m = (MathView)findViewById(R.id.math);

        TextView v = (TextView)findViewById(R.id.editText);
        m.setDisplayText(v.getText().toString());

        String simpson = "function simpson(a,b,n,func,dvar)" +
                "{" +
                "h = (b-a)/n;" +
                "k1 = 0;" +
                "k2=0;" +
                "for(i=1;i<n;i=i+h)" +
                "{" +
                "k1 = k1 + func(dvar = 1);" +
                "k2 = k2 + func(dvar = 2);" +
                "}" +
                "return h/3*(func(dvar=a)+4*k1+2*k2);" +
                "}";

        new Interpretator().interpret(new Parser(new Lexer().lex(v.getText().toString())).parse());
    }
}
