package team.air.mathsoluter.Activities.CustomKeyboard;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CustomKeyboardPageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public CustomKeyboardTemplate tab1, tab2;

    public CustomKeyboardPageAdapter(FragmentManager fm , int numOfTabs)
    {
        super(fm);
        this.numOfTabs = numOfTabs;
        tab1 = new CustomKeyboardTemplate();
        tab2 = new CustomKeyboardTemplate();
    }

    @Override
    public Fragment getItem(int i) {

        switch (i)
        {
            case 0:
                return tab1;
            case 1:
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
