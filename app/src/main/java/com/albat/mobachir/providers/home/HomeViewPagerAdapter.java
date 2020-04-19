package com.albat.mobachir.providers.home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.albat.mobachir.providers.fixture.ui.FixturesHomeFragment;
import com.albat.mobachir.providers.guess.ui.GuessFragment;
import com.albat.mobachir.providers.leagues.ui.LeaguesFragment;
import com.albat.mobachir.providers.news.ui.NewsFragment;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {
    private static int COUNT = 4;
    NewsFragment newsFragment = new NewsFragment();
    FixturesHomeFragment fixturesHomeFragment = new FixturesHomeFragment();
    LeaguesFragment leaguesFragment = new LeaguesFragment();
    GuessFragment guessFragment = new GuessFragment();

    public HomeViewPagerAdapter(FragmentManager fragmentManager) {
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
                return newsFragment;
            case 1:
                return fixturesHomeFragment;
            case 2:
                return leaguesFragment;
            case 3:
                return guessFragment;
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
            case 3:
                return "";
            default:
                return "";
        }
    }
}