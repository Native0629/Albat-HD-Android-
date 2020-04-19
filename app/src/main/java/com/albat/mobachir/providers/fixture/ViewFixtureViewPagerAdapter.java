package com.albat.mobachir.providers.fixture;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.providers.fixture.ui.CommentsFragment;
import com.albat.mobachir.providers.fixture.ui.FixtureEventsFragment;
import com.albat.mobachir.providers.fixture.ui.FixtureInfoFragment;
import com.albat.mobachir.providers.fixture.ui.FixtureLineupFragment;
import com.albat.mobachir.providers.fixture.ui.FixtureStatsFragment;

/**
 * Created by Mohey on 05/18/2018.
 */
public class ViewFixtureViewPagerAdapter extends FragmentPagerAdapter {
    private int count = 5;
    FixtureInfoFragment fixtureInfoFragment = new FixtureInfoFragment();
    CommentsFragment commentsFragment = new CommentsFragment();
    FixtureLineupFragment fixtureLineupFragment = new FixtureLineupFragment();
    FixtureStatsFragment fixtureStatsFragment = new FixtureStatsFragment();
    FixtureEventsFragment fixtureEventsFragment = new FixtureEventsFragment();

    Fixture fixture;

    public ViewFixtureViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return count;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        if (count == 5) {
            switch (position) {
                case 0:
                    return fixtureInfoFragment;
                case 1:
                    return fixtureLineupFragment;
                case 2:
                    return fixtureStatsFragment;
                case 3:
                    return fixtureEventsFragment;
                case 4:
                    return commentsFragment;
                default:
                    return null;
            }
        } else {
            switch (position) {
                case 0:
                    return fixtureInfoFragment;
                case 1:
                    return fixtureLineupFragment;
                case 2:
                    return fixtureStatsFragment;
                case 3:
                    return fixtureEventsFragment;
                case 4:
                    return commentsFragment;
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

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;

        fixtureInfoFragment.setFixture(fixture);
        commentsFragment.setFixture(fixture);
        fixtureStatsFragment.setFixture(fixture);
        fixtureLineupFragment.setFixture(fixture);
        fixtureEventsFragment.setFixture(fixture);
//        topScorerFragment.setLeague(fixture);
//        fixturesFragment.setLeague(fixture);
//        standingsFragment.setLeague(fixture);
//        rssFragment.setUrl(fixture.rss);
    }

}