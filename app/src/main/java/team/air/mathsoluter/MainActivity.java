package team.air.mathsoluter;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.google.android.material.tabs.TabLayout;
import com.myscript.iink.Configuration;
import com.myscript.iink.ContentPackage;
import com.myscript.iink.ContentPart;
import com.myscript.iink.ConversionState;
import com.myscript.iink.Editor;
import com.myscript.iink.Engine;
import com.myscript.iink.IEditorListener;
import com.myscript.iink.uireferenceimplementation.EditorView;
import com.myscript.iink.uireferenceimplementation.FontUtils;
import com.myscript.iink.uireferenceimplementation.InputController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import team.air.mathsoluter.Activities.CustomKeyboard.CallContainerWithView;
import team.air.mathsoluter.Activities.CustomKeyboard.CustomKeyboardPageAdapter;
import team.air.mathsoluter.Core.System.Lexer.Lexer;
import team.air.mathsoluter.Core.System.Parser.Interpretator;
import team.air.mathsoluter.Core.System.Parser.Parser;
import team.air.mathsoluter.Core.Util.CallContainer;
import team.air.mathsoluter.MyScript.MySctiptManager;

public class MainActivity extends AppCompatActivity  {

    Activity thisActivity;
    ViewPager keyboardPager;
    CustomKeyboardPageAdapter customKeyboardPageAdapter;
    View keyboardLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisActivity = this;
        keyboardLayout = findViewById(R.id.keyboardLayout);
        TabLayout tabLayout = findViewById(R.id.tabBar);

        //настраиваем фрагменты окон
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


        //настраиваем фрагменты клавиатуры

        final TabLayout keyTabLayout = findViewById(R.id.keyboardTabBar);
        keyboardPager = findViewById(R.id.keyboardViewPager);

        customKeyboardPageAdapter = new CustomKeyboardPageAdapter(getSupportFragmentManager() , keyTabLayout.getTabCount());
        keyboardPager.setAdapter(customKeyboardPageAdapter);

        keyTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                keyboardPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        customKeyboardPageAdapter.tab1.callContainer = new CallContainer() {
            @Override
            public void call() {
                customKeyboardPageAdapter.tab1.init(thisActivity, R.xml.custom_keyboard_tab1, keyboardLayout);
               // customKeyboardPageAdapter.tab1.registerEditText((EditText)findViewById(R.id.scriptTextView));
            }
        };

        customKeyboardPageAdapter.tab2.callContainer = new CallContainer() {
            @Override
            public void call() {
                customKeyboardPageAdapter.tab2.init(thisActivity, R.xml.custom_keyboard_tab1, keyboardLayout);
              //  customKeyboardPageAdapter.tab2.registerEditText((EditText)findViewById(R.id.scriptTextView));
            }
        };





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

        //CustomKeyboardPageAdapter customKeyboardActivity = findViewById(R.id.keyboardViewPager).findViewById();
    }



    public void click(View view)
    {
        TextView v = (TextView)findViewById(R.id.scriptEditText);
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
        if( keyboardLayout.getVisibility() == View.VISIBLE ) keyboardLayout.setVisibility(View.GONE); else this.finish();
    }

}


