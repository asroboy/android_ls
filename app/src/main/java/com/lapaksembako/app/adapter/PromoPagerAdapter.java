package com.lapaksembako.app.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lapaksembako.app.fragments.PlaceholderFragment;
import com.lapaksembako.app.fragments.PromoFragment;
import com.lapaksembako.app.model.Slider;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class PromoPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    ArrayList<Slider> mSliders;

    public PromoPagerAdapter(Context context, FragmentManager fm, ArrayList<Slider> sliders) {
        super(fm);
        mContext = context;
        mSliders = sliders;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PromoFragment.newInstance(position + 1, mSliders.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return mSliders.size();
    }
}