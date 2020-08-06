package team.air.mathsoluter.Activities.CustomKeyboard;

import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import team.air.mathsoluter.R;
import team.air.mathsoluter.ScriptFragment;

public class CustomKeyboardActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_keyboard_layout);

        TabLayout tabLayout = findViewById(R.id.keyboardTabBar);

        final ViewPager viewPager = findViewById(R.id.keyboardViewPager);

        CustomKeyboardPageAdapter pagerAdapter =
                new CustomKeyboardPageAdapter(getSupportFragmentManager() , tabLayout.getTabCount());
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

    }


}
class CustomKeyboardPageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public CustomKeyboardPageAdapter(FragmentManager fm , int numOfTabs)
    {
        super(fm);
        this.numOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int i) {

        switch (i)
        {
            case 0:
                return new CustomKeyboardTemplate();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
