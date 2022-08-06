package com.syndicate.lead.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.syndicate.lead.fragment.CommissionFragment;
import com.syndicate.lead.fragment.LiveLeadsFragment;
import com.syndicate.lead.fragment.LeadsFragmentA;
import com.syndicate.lead.fragment.MyLeadsFragment;
import com.syndicate.lead.fragment.ReceivedLeadsFragment;

/**
 * Created by Priyabrat on 21-08-2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new LeadsFragmentA();
        }
        else if (position == 1)
        {
            fragment = new MyLeadsFragment();
        }
        else if (position == 2)
        {
            fragment = new LiveLeadsFragment();
        }
        else if (position == 3)
        {
            fragment = new ReceivedLeadsFragment();
        }
        else if (position == 4)
        {
            fragment = new CommissionFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Lead";
        }
        else if (position == 1)
        {
            title = "My Lead";
        }
        else if (position == 2)
        {
            title = "Live Lead";
        }
        else if (position == 3)
        {
            title = "My Bid";
        }
        else if (position == 4)
        {
            title = "Incentive";
        }
        return title;
    }
}
