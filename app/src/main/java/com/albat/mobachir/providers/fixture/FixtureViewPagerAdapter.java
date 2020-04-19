package com.albat.mobachir.providers.fixture;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.albat.mobachir.providers.fixture.ui.FixturesFragment;

import java.util.Date;

public class FixtureViewPagerAdapter extends FragmentPagerAdapter {
    private static int COUNT = 3;
    FixturesFragment allFixturesFragment = new FixturesFragment().setMode(FixturesFragment.Mode.ALL);
    FixturesFragment importantFixturesFragment = new FixturesFragment().setMode(FixturesFragment.Mode.IMPORTANT);
    FixturesFragment favoriteFixturesFragment = new FixturesFragment().setMode(FixturesFragment.Mode.FAVORITE);

    public FixtureViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return COUNT;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return allFixturesFragment;
            case 1:
                return importantFixturesFragment;
            case 2:
                return favoriteFixturesFragment;
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "";
            case 1:
                return "";
            case 2:
                return "";
            default:
                return "";
        }
    }

    public void requestFixtures(Date date) {
        allFixturesFragment.updateFixtures(date);
        importantFixturesFragment.updateFixtures(date);
        favoriteFixturesFragment.updateFixtures(date);
    }
}