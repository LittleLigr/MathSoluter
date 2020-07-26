package team.air.mathsoluter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter
{
    private int numOfTabs;

    public PagerAdapter(FragmentManager fm , int numOfTabs)
    {
        super(fm);
        this.numOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int i) {

        switch (i)
        {
            case 0:
                return new ScriptFragment();
            case 1:
                return new ConsoleFragment();
            case 2:
                return new SourceFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
