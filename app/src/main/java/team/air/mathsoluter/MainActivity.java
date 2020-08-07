package team.air.mathsoluter;

import android.content.Intent;
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
import android.widget.TextView;

import team.air.mathsoluter.Core.System.Lexer.Lexer;
import team.air.mathsoluter.Core.System.Parser.Interpretator;
import team.air.mathsoluter.Core.System.Parser.Parser;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabLayout tabLayout = findViewById(R.id.tabBar);
        TabItem tabScript = findViewById(R.id.scriptId);
        TabItem tabConsole = findViewById(R.id.consoleId);
        TabItem tabSource = findViewById(R.id.sourceId);
         TabItem tabPlot = findViewById(R.id.plotId);

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
                "Script", "Console", "Source code", "Plot"
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

        //Intent intent = new Intent(this, ListViewOfMethods.class);
        //startActivity(intent);

    }

    public void click(View view)
    {
        TextView v = (TextView)findViewById(R.id.scriptTextView);
        TextView console = (TextView)findViewById(R.id.consoleTextView);
        new Interpretator().interpret(new Parser(new Lexer().lex(v.getText().toString())).parse(console));
    }

    public void s()
    {

    }
}


