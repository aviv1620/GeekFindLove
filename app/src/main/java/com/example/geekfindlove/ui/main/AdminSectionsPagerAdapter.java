package com.example.geekfindlove.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.geekfindlove.AdminNewQuestionFragment;
import com.example.geekfindlove.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class AdminSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.fragment_admin_new_questions_title,R.string.fragment_admin_edit_questions_title,R.string.fragment_admin_new_pickup_title,R.string.fragment_admin_edit_pickup_title  };
    private final Context mContext;

    public AdminSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // newInsatance(number) the number is the amount of columns we want in the list that is shown in the layout
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return AdminNewQuestionFragment.newInstance();

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