package com.example.jasmine.goalachieverassistant.Fragments.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.jasmine.goalachieverassistant.Fragments.Fragments.GoalDetailsFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.GoalTasksFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.TaskDetailsFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.TaskSubTaskListFragment;
import com.example.jasmine.goalachieverassistant.R;

/**
 * Created by jasmine on 15/02/18.
 */

public class CustomPagerFragmentAdapterTask extends FragmentStatePagerAdapter {

    private Context mContext;
    private String extraInfo;




    public CustomPagerFragmentAdapterTask(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

    }

    public CustomPagerFragmentAdapterTask(Context context, FragmentManager fm, String extra) {
        super(fm);
        mContext = context;
        extraInfo = extra;

    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {

            Log.d("GOALS", "goalbeeVIEWTYPE.TASKVIEW2  " );
            if (position == 0) {
                Log.d("GOALS", "1goalbeee");
                return TaskDetailsFragment.newInstance(extraInfo);
            } else if (position == 1){
                Log.d("GOALS", "2goalbee" );
                return TaskSubTaskListFragment.newInstance(extraInfo);
            } else {
                Log.d("GOALS", "3goalbee" );
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