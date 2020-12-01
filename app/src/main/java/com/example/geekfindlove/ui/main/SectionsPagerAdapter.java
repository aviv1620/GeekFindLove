package com.example.geekfindlove.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.geekfindlove.MatchingFragment;
import com.example.geekfindlove.PickUpLineFragment;
import com.example.geekfindlove.R;
import com.example.geekfindlove.profile_fragment;
import com.example.geekfindlove.questions_fragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.fragment_matching_title, R.string.fragment_question_title,R.string.fragment_pickupline_title,R.string.fragment_profile_title};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MatchingFragment.newInstance(1);
            case 1:
                return  questions_fragment.newInstance();
            case 2:
                return PickUpLineFragment.newInstance(1);
            case 3:
                return profile_fragment.newInstance();

        }
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}