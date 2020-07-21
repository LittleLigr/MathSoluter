package team.air.mathsoluter;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import katex.hourglass.in.mathlib.MathView;

public class MainActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Fragment> list = new ArrayList<>();
        list.add(new MainFragment());
        list.add(new ExpressionFragment());
        pager = findViewById(R.id.pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager() , list);
        pager.setAdapter(pagerAdapter);
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.pager , new MainFragment())
//                .add(R.id.pager , new ExpressionFragment())
//                .commit();

        MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    public void onClickButtonFragment()
    {

    }
}
