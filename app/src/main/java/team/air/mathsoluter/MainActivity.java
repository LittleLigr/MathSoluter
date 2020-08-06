package team.air.mathsoluter;

import android.graphics.drawable.Drawable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import team.air.mathsoluter.Core.System.Lexer.Lexer;
import team.air.mathsoluter.Core.System.Parser.Interpretator;
import team.air.mathsoluter.Core.System.Parser.Parser;

public class MainActivity extends AppCompatActivity  {

    static CustomKeyboard mCustomKeyboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabBar);
        TabItem tabSoript = findViewById(R.id.scriptId);
        TabItem tabConsole = findViewById(R.id.consoleId);
        final TabItem tabSource = findViewById(R.id.sourceId);

        final ViewPager viewPager = findViewById(R.id.viewPager);


        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager() , tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        int[] imageResId = {
                R.drawable.errorview, R.drawable.noticeview
        };

        String [] tabNames = {
                "Script", "Console", "Source code"
        };

        for (int i = 0; i < imageResId.length; i++) {
            Drawable image = getBaseContext().getResources().getDrawable(imageResId[i]);
            image.setBounds(0, 0, 32, 32);
            // заменяем пробел иконкой
            SpannableString sb = new SpannableString("   " + tabNames[i]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tabLayout.getTabAt(i).setText(sb);
        }

        //mCustomKeyboard= new CustomKeyboard(this, R.id.keyboard, R.xml.hexkbd );


    }

    public void click(View view)
    {
        TextView v = (TextView)findViewById(R.id.scriptTextView);
        TextView console = (TextView)findViewById(R.id.consoleTextView);

        String simps = "function Simpson(a,b,n,expr,varv){" +
                "var h = (b-a)/n;" +
                "var k1 = 0;" +
                "var k2 = 0;" +
                "for(var i = 1; i < n; i =i+ h){" +
                "expr.setVar(varv, a+i*h);" +
                "k1 = k1 + expr.solve();" +
                "expr.setVar(varv, a+(i+1)*h);" +
                "k2 = k2+expr.solve();" +
                "}" +
                "expr.setVar(varv, a);" +
                "return h/3*(expr.solve()+4*k1+2*k2;" +
                "}";

        new Interpretator().interpret(new Parser(new Lexer().lex(simps+v.getText().toString())).parse(console));
    }

    @Override public void onBackPressed() {
        // NOTE Trap the back key: when the CustomKeyboard is still visible hide it, only when it is invisible, finish activity
       // if( mCustomKeyboard.isCustomKeyboardVisible() ) mCustomKeyboard.hideCustomKeyboard(); else this.finish();
    }

}


