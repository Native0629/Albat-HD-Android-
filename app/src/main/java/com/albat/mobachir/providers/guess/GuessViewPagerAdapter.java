package com.albat.mobachir.providers.guess;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.albat.mobachir.providers.guess.ui.AddGuessFragment;
import com.albat.mobachir.providers.guess.ui.LeaderboardFragment;
import com.albat.mobachir.providers.guess.ui.MyGuessesFragment;
import com.albat.mobachir.providers.guess.ui.WinnersPrizesFragment;

public class GuessViewPagerAdapter extends FragmentPagerAdapter {
    private static int COUNT = 4;
    MyGuessesFragment myGuessesFragment = new MyGuessesFragment();
    WinnersPrizesFragment winnersPrizesFragment = new WinnersPrizesFragment();
    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
    AddGuessFragment addGuessFragment = new AddGuessFragment();

    public GuessViewPagerAdapter(FragmentManager fragmentManager) {
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
                return addGuessFragment;
            case 1:
                return winnersPrizesFragment;
            case 2:
                return leaderboardFragment;
            case 3:
                return myGuessesFragment;
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