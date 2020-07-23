package team.air.mathsoluter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MainFragment.SendMessage {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        List<Fragment> list = new ArrayList<>();
//        list.add(new MainFragment());
//        list.add(new ExpressionFragment());
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void sendData(String message) {

    }

    private class ViewPagerAdapter {
        public FragmentManager ViewPagerAdapter(FragmentManager fragmentManager)
        {
            return fragmentManager;
        }
    }
}
