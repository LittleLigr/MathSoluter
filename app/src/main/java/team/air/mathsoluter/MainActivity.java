package team.air.mathsoluter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import team.air.mathsoluter.Core.System.Lexer.Lexer;
import team.air.mathsoluter.Core.System.Token;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view)
    {
        System.out.println("TEST");
        TextView v = (TextView)findViewById(R.id.editText);
        ArrayList<Token> tokens = new Lexer().lex(v.getText().toString());
        for(Token t :tokens)
            System.out.println(t.toString());
    }
}
