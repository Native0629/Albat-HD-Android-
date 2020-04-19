package com.albat.mobachir.providers.leagues;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.albat.mobachir.network.models.League;
import com.albat.mobachir.providers.leagues.ui.FixturesFragment;
import com.albat.mobachir.providers.leagues.ui.StandingsFragment;
import com.albat.mobachir.providers.leagues.ui.TopScorerFragment;
import com.albat.mobachir.providers.rss.ui.CRssFragment;

/**
 * Created by Mohey on 05/18/2018.
 */
public class LeagueViewPagerAdapter extends FragmentPagerAdapter {
    private int count = 4;
    TopScorerFragment topScorerFragment = new TopScorerFragment();
    FixturesFragment fixturesFragment = new FixturesFragment();
    StandingsFragment standingsFragment = new StandingsFragment();
    CRssFragment rssFragment = new CRssFragment();

    League league;

    public LeagueViewPagerAdapter(FragmentManager fragmentManager, int count) {
        super(fragmentManager);
        this.count = count;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return count;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        if (count == 3) {
            switch (position) {
                case 0:
                    return fixturesFragment;
                case 1:
                    return standingsFragment;
                case 2:
                    return topScorerFragment;
                default:
                    return null;
            }
        } else {
            switch (position) {
                case 0:
                    return rssFragment;
                case 1:
                    return fixturesFragment;
                case 2:
                    return standingsFragment;
                case 3:
                    return topScorerFragment;
                default:
                    return null;
            }
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

    public void setLeague(League league) {
        this.league = league;

        topScorerFragment.setLeague(league);
        fixturesFragment.setLeague(league);
        standingsFragment.setLeague(league);
        rssFragment.setUrl(league.rss);
    }

}