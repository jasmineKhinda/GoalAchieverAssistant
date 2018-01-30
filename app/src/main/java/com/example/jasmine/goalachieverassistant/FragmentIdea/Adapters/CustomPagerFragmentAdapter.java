package com.example.jasmine.goalachieverassistant.FragmentIdea.Adapters;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.example.jasmine.goalachieverassistant.FragmentIdea.Fragments.GoalDetailsFragment;
import com.example.jasmine.goalachieverassistant.FragmentIdea.Fragments.GoalTasksFragment;
import com.example.jasmine.goalachieverassistant.R;


/**
 * Created by jasmine on 23/01/18.
 */

public class CustomPagerFragmentAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private String extraInfo;



    public CustomPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public CustomPagerFragmentAdapter(Context context, FragmentManager fm, String extra) {
        super(fm);
        mContext = context;
        extraInfo = extra;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return GoalDetailsFragment.newInstance(extraInfo);
        } else if (position == 1){
            return GoalTasksFragment.newInstance(extraInfo);
        } else {
            return null;
        }
    }


    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab1);
            case 1:
                return mContext.getString(R.string.tab2);
            default:
                return null;
        }
    }

}