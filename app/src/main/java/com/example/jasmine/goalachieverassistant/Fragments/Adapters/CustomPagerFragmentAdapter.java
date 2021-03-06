package com.example.jasmine.goalachieverassistant.Fragments.Adapters;

import android.os.Parcelable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.example.jasmine.goalachieverassistant.Fragments.Fragments.GoalDetailsFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.GoalTasksFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.TaskDetailsFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.TaskSubTaskListFragment;
import com.example.jasmine.goalachieverassistant.R;


/**
 * Created by jasmine on 23/01/18.
 */

public class CustomPagerFragmentAdapter extends FragmentStatePagerAdapter   {

   // CustomPagerFragmentAdapter adapter;
    private Context mContext;
    private String extraInfo;

//    VIEWTYPE viewType=null;
//
//    public enum VIEWTYPE {
//        GOALVIEW, TASKVIEW;
//    }



    public CustomPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

    }

    public CustomPagerFragmentAdapter(Context context, FragmentManager fm, String extra) {
        super(fm);
        mContext = context;
        extraInfo = extra;
//        viewType =view;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
 //       Log.d("GOALS", "IN cusom pager getITem "+ viewType);

 //       if(VIEWTYPE.GOALVIEW== viewType){
            Log.d("GOALS", "goalbeeVIEWTYPE.GOALVIEW1  " );
            if (position == 0) {
                Log.d("GOALS", "goalbee1a");
                return GoalDetailsFragment.newInstance(extraInfo);
            } else if (position == 1){
                Log.d("GOALS", "goalbee2a");
                return GoalTasksFragment.newInstance(extraInfo);
            } else {
                Log.d("GOALS", "goalbee3a");
                return null;
            }
//        }else if(VIEWTYPE.TASKVIEW == viewType){
//            Log.d("GOALS", "goalbeeVIEWTYPE.TASKVIEW2  "+ VIEWTYPE.TASKVIEW );
//            if (position == 0) {
//                Log.d("GOALS", "1goalbeee"+ VIEWTYPE.GOALVIEW );
//                return TaskDetailsFragment.newInstance(extraInfo);
//            } else if (position == 1){
//                Log.d("GOALS", "2goalbee"+ VIEWTYPE.GOALVIEW );
//                return TaskSubTaskListFragment.newInstance(extraInfo);
//            } else {
//                Log.d("GOALS", "3goalbee"+ VIEWTYPE.GOALVIEW );
//                return null;
//            }
//        }else{
//            Log.e("ERROR", "ViewType doesn't match the listed options");
//            return null;
//        }


    }

//    @Override
//    public int getItemPosition(Object object) {
//        if (object instanceof GoalDetailsFragment) {
//            // Create a new method notifyUpdate() in your fragment
//            // it will get call when you invoke
//            // notifyDatasetChaged();
//            ((CustomPagerFragmentAdapter) object).notifyUpdate();
//        }
//        //don't return POSITION_NONE, avoid fragment recreation.
//        return super.getItemPosition(object);
//    }
////
////
////    public void notifyUpdate(){
////        Log.d("GOALS", "notifyUpdate2: ");
////        notifyDataSetChanged();
////
////
////    }



    public int getItemPosition(Object item) {

//        if (item instanceof GoalDetailsFragment) {
//            Log.d("GOALS", "getItemPosition:1 ");
//            return 0;
//
//        } else if(item instanceof GoalTasksFragment){
//            Log.d("GOALS", "getItemPosition:2 ");
//            return 1;
//        } else{
//            Log.d("GOALS", "getItemPosition:3 ");
            return POSITION_NONE;
 //       }
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

//    public void refresh(){
//        adapter.notifyDataSetChanged();
//    }



}