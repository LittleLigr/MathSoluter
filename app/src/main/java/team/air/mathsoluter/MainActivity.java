package team.air.mathsoluter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import katex.hourglass.in.mathlib.MathView;
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
        v.setText("for(var a = 0; a < 5; a = a+1)" +
                "print a;");
        System.out.println(v.getText());
    }

    public void click(View view)
    {
        MathView katex = findViewById(R.id.math);
        TextView v = (TextView)findViewById(R.id.editText);
        katex.setDisplayText("$$"+v.getText().toString()+"$$");
    }
}
