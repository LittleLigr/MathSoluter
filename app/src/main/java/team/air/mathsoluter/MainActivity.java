package team.air.mathsoluter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabLayout tabLayout = findViewById(R.id.tabBar);
        TabItem tabSoript = findViewById(R.id.scriptId);
        TabItem tabConsole = findViewById(R.id.consoleId);
        TabItem tabSource = findViewById(R.id.sourceId);

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


