package com.albat.mobachir;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.albat.mobachir.drawer.MenuItemCallback;
import com.albat.mobachir.drawer.NavItem;
import com.albat.mobachir.drawer.SimpleMenu;
import com.albat.mobachir.drawer.TabAdapter;
import com.albat.mobachir.inherit.BackPressFragment;
import com.albat.mobachir.inherit.CollapseControllingFragment;
import com.albat.mobachir.inherit.ConfigurationChangeFragment;
import com.albat.mobachir.inherit.PermissionsFragment;
import com.albat.mobachir.network.interfaces.UserUpdated;
import com.albat.mobachir.network.managers.UserManager;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.network.models.User;
import com.albat.mobachir.providers.CustomIntent;
import com.albat.mobachir.providers.fav.ui.FavFragment;
import com.albat.mobachir.providers.fixture.ui.FixturesNowFragment;
import com.albat.mobachir.providers.worldcup.ui.WorldCupFragment;
import com.albat.mobachir.util.CLog;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.Helper;
import com.albat.mobachir.util.IntentLaunchers;
import com.albat.mobachir.util.Log;
import com.albat.mobachir.util.SharedPreferencesManager;
import com.albat.mobachir.util.layout.CustomAppBarLayout;
import com.albat.mobachir.util.layout.DisableableViewPager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.egslava.blurredview.BlurredImageView;

/**
 * This file is part of the Universal template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2017
 */
public class MainActivity extends BaseActivity implements MenuItemCallback, ConfigParser.CallBack, RewardedVideoAdListener, UserUpdated {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUESTCODE = 123;

    //Layout
    public Toolbar mToolbar;
    private TabLayout tabLayout;
    private DisableableViewPager viewPager;
    private NavigationView navigationView;
    public DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private boolean nowTabOpened = false;
    //Adapters
    private TabAdapter adapter;
    private static SimpleMenu menu;

    //Keep track of the interstitials we show
    private int interstitialCount = -1;

    //Data to pass to a fragment
    public static String FRAGMENT_DATA = "transaction_data";
    public static String FRAGMENT_CLASS = "transation_target";

    //Permissions Queu
    List<NavItem> queueItem;
    int queueMenuItemId;

    //InstanceState (rotation)
    private Bundle savedInstanceState;
    private static final String STATE_MENU_INDEX = "MENUITEMINDEX";
    private static final String STATE_PAGER_INDEX = "VIEWPAGERPOSITION";
    private static final String STATE_ACTIONS = "ACTIONS";
    private InterstitialAd mInterstitialAd;
    private boolean isRunning;

    private Handler mHandler;       // Handler to display the ad on the UI thread
    private Runnable displayAd;

    SharedPreferencesManager sharedPreferencesManager;
    App app;
    DialogHelper dialogHelper;

    private TextView toolbarGoldenPoints;

    @Override
    public void configLoaded(boolean facedException) {
        if (facedException || menu.getFirstMenuItem() == null) {
            if (Helper.isOnlineShowDialog(MainActivity.this))
                Toast.makeText(this, R.string.invalid_configuration, Toast.LENGTH_LONG).show();
        } else {
            if (savedInstanceState == null) {
                menuItemClicked(menu.getFirstMenuItem(), 0, false);
            } else {
                ArrayList<NavItem> actions = (ArrayList<NavItem>) savedInstanceState.getSerializable(STATE_ACTIONS);
                int menuItemId = savedInstanceState.getInt(STATE_MENU_INDEX);
                int viewPagerPosition = savedInstanceState.getInt(STATE_PAGER_INDEX);

                menuItemClicked(actions, menuItemId, false);
                viewPager.setCurrentItem(viewPagerPosition);
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar

        this.savedInstanceState = savedInstanceState;
        dialogHelper = new DialogHelper();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        app = App.getInstance();

        //Load the appropriate layout
//        if (useTabletMenu()) {
//            setContentView(R.layout.activity_main_tablet);
//            Helper.setStatusBarColor(MainActivity.this,
//                    ContextCompat.getColor(this, R.color.myPrimaryDarkColor));
//        } else {
//            setContentView(R.layout.activity_main);
//        }

        setContentView(R.layout.activity_main);
        AppRater.app_launched(this);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (!useTabletMenu())
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        else {
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        //Drawer
        if (!useTabletMenu()) {
            drawer = findViewById(R.id.drawer);
            toggle = new ActionBarDrawerToggle(
                    this, drawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        }

        // Change Languige
//        Button changeLang = findViewById(R.id.changeLang);
//        changeLang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showChangeLanguageDialog();
//            }
//        });


        //Layouts
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        //Check if we should open a fragment based on the arguments we have
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(FRAGMENT_CLASS)) {
            try {
                Class<? extends Fragment> fragmentClass = (Class<? extends Fragment>) getIntent().getExtras().getSerializable(FRAGMENT_CLASS);
                if (fragmentClass != null) {
                    String[] extra = getIntent().getExtras().getStringArray(FRAGMENT_DATA);

                    HolderActivity.startActivity(this, fragmentClass, extra);
                    finish();
                    //Optionally, we can also point intents to holderactivity directly instead of MainAc.
                }
            } catch (Exception e) {
                //If we come across any errors, just continue and open the default fragment
                Log.printStackTrace(e);
            }
        }

        //Menu items
        navigationView = findViewById(R.id.nav_view);
        toolbarGoldenPoints = findViewById(R.id.toolbarGoldenPoints);
        updateHeader();

        findViewById(R.id.toolbarNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nowTabOpened) {
                    nowTabOpened = true;
                    Fragment fragment = new FixturesNowFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));
                }
            }
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = Config.SHARE_TEXT + "\n" + Config.GOOGLE_PLAY_APP_URL + getPackageName();
                IntentLaunchers.startShareTextActivity(MainActivity.this, "مشاركة تطبيق البث المباشر", text);
            }
        });

        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.REFRESH));
            }
        });

        if (sharedPreferencesManager.isLoggedIn()) {
            findViewById(R.id.goldenPointsLayout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.goldenPointsLayout).setVisibility(View.GONE);
        }


        menu = new SimpleMenu(navigationView.getMenu(), this);
        if (Config.USE_HARDCODED_CONFIG) {
            Config.configureMenu(menu, this);
        } else if (!Config.CONFIG_URL.isEmpty() && Config.CONFIG_URL.contains("http"))
            new ConfigParser(Config.CONFIG_URL, menu, this, this).execute();
        else
            new ConfigParser("config.json", menu, this, this).execute();

        tabLayout.setupWithViewPager(viewPager);

        if (!useTabletMenu()) {
            drawer.setStatusBarBackgroundColor(
                    ContextCompat.getColor(this, R.color.myPrimaryDarkColor));
        }

        applyDrawerLocks();

        Helper.admobLoader(this, findViewById(R.id.adView));
        Helper.updateAndroidSecurityProvider(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                onTabBecomesActive(position);
            }
        });

        mHandler = new Handler(Looper.getMainLooper());

        displayAd = new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }
                });
            }
        };

//        loadAd();
        adRequest();

        initializeRewardVideo();
    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"English", "Arabic", "Spanish"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose Language ... ");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i==0){
                    setLocale("ar");
                    recreate();
                }
                if (i==1){
                    setLocale("en");
                    recreate();
                }
                if (i==2){
                    setLocale("es");
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs  = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    private RewardedVideoAd mRewardedVideoAd;

    private void initializeRewardVideo() {
        MobileAds.initialize(this, getString(R.string.admob_video_id));

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.admob_video_id),
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        CLog.e(TAG, "onRewarded");
        dialogHelper.showLoadingDialog(this, "اضافة القطع الذهبية");

        UserManager userManager = new UserManager();
        userManager.addCoin(this);
    }

    @Override
    public void onUserUpdated(User user, boolean success, String error) {
        dialogHelper.hideLoadingDialog();
        dialogHelper.hideLoadingDialogSuccess(this, "مبروك!", "تمت اضافة القطع الذهبية");

        if (!success || user == null) {
            user = app.getUser();
            user.goldenPoints += 3;
        }

        sharedPreferencesManager.saveUser(user);
        App.getInstance().setUser(user);

        EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_COINS));
    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        // Load the next rewarded video ad.
        loadRewardedVideoAd();
    }

    public void adRequest() {

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_id));

        AdRequest adRequestInter = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequestInter);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
//                mInterstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                loadAd();
            }
        });
    }

    public void displayInterstitial() {
        mHandler.postDelayed(displayAd, 1);
    }

    void loadAd() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Load the adView object witht he request
        mInterstitialAd.loadAd(adRequest);
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUESTCODE:
                boolean allGranted = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                    }
                }
                if (allGranted) {
                    //Retry to open the menu item
                    menuItemClicked(queueItem, queueMenuItemId, false);
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.permissions_required), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRunning = true;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void menuItemClicked(List<NavItem> actions, int menuItemIndex, boolean requiresPurchase) {
        // Checking the drawer should be open on start

        //Show in interstitial
        showInterstitial();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean openOnStart = Config.DRAWER_OPEN_START || prefs.getBoolean("menuOpenOnStart", false);
        if (drawer != null) {
            boolean firstClick = (savedInstanceState == null && adapter == null);
            if (openOnStart && !useTabletMenu() && firstClick) {
                drawer.openDrawer(GravityCompat.START);
            } else {
                //Close the drawer
                drawer.closeDrawer(GravityCompat.START);
            }
        }

        //Check if the user is allowed to open item
        if (requiresPurchase && !isPurchased()) return; //isPurchased will handle this.
        if (!checkPermissionsHandleIfNeeded(actions, menuItemIndex))
            return; //checkPermissions will handle.

        if (isCustomIntent(actions)) return;

        getSupportFragmentManager().popBackStack();
        nowTabOpened = false;
        //Uncheck all other items, check the current item
        for (MenuItem menuItem : menu.getMenuItems()) {
            if (menuItem.getItemId() == menuItemIndex) {
                menuItem.setChecked(true);
            } else
                menuItem.setChecked(false);
        }

        //Load the new tab
        boolean isRtl = ViewCompat.getLayoutDirection(tabLayout) == ViewCompat.LAYOUT_DIRECTION_RTL;
        adapter = new TabAdapter(getSupportFragmentManager(), actions, this, isRtl);
        viewPager.setAdapter(adapter);

        //Show or hide the tab bar depending on if we need it
        if (actions.size() == 1) {
            tabLayout.setVisibility(View.GONE);
            viewPager.setPagingEnabled(false);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setPagingEnabled(true);
        }
        ((CustomAppBarLayout) mToolbar.getParent()).setExpanded(true, true);

        onTabBecomesActive(0);
    }

    private void onTabBecomesActive(int position) {
        Fragment fragment = adapter.getItem(position);
        //If fragment does not support collapse, or if OS does not support collapse, disable collapsing toolbar
        if ((fragment instanceof CollapseControllingFragment
                && !((CollapseControllingFragment) fragment).supportsCollapse())
                ||
                (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) || fragment instanceof WorldCupFragment || fragment instanceof BaseFragment)
            lockAppBar();
        else
            unlockAppBar();

        if (position != 0)
            showInterstitial();
    }

    /**
     * Show an interstitial ad
     */
    public void showInterstitial() {
        //if (fromPager) return;
        if (getResources().getString(R.string.admob_interstitial_id).length() == 0) return;
        if (SettingsFragment.getIsPurchased(this)) return;

        if (interstitialCount == (Config.INTERSTITIAL_INTERVAL - 1)) {

            displayInterstitial();

//            if (mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//            }

            interstitialCount = 0;
        } else {
            interstitialCount++;
        }

    }

    /**
     * Checks if the item is/contains a custom intent, and if that the case it will handle it.
     *
     * @param items List of NavigationItems
     * @return True if the item is a custom intent, in that case
     */
    private boolean isCustomIntent(List<NavItem> items) {
        NavItem customIntentItem = null;
        for (NavItem item : items) {
            if (CustomIntent.class.isAssignableFrom(item.getFragment())) {
                customIntentItem = item;
            }
        }

        if (customIntentItem == null) return false;
        if (items.size() > 1)
            Log.e("INFO", "Custom Intent Item must be only child of menu item! Ignorning all other tabs");

        CustomIntent.performIntent(MainActivity.this, customIntentItem.getData());
        return true;
    }

    /**
     * If the item can be opened because it either has been purchased or does not require a purchase to show.
     *
     * @return true if the app is purchased. False if the app hasn't been purchased, or if iaps are disabled
     */
    private boolean isPurchased() {
        String license = getResources().getString(R.string.google_play_license);
        // if item does not require purchase, or app has purchased, or license is null/empty (app has no in app purchases)
        if (!SettingsFragment.getIsPurchased(this) && !license.equals("")) {
            String[] extra = new String[]{SettingsFragment.SHOW_DIALOG};
            HolderActivity.startActivity(this, SettingsFragment.class, extra);

            return false;
        }

        return true;
    }

    /**
     * Checks if the item can be opened because it has sufficient permissions.
     *
     * @param tabs The tabs to check
     * @return true if the item is safe to open
     */
    private boolean checkPermissionsHandleIfNeeded(List<NavItem> tabs, int menuItemId) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) return true;

        List<String> allPermissions = new ArrayList<>();
        for (NavItem tab : tabs) {
            if (PermissionsFragment.class.isAssignableFrom(tab.getFragment())) {
                try {
                    for (String permission : ((PermissionsFragment) tab.getFragment().newInstance()).requiredPermissions()) {
                        if (!allPermissions.contains(permission))
                            allPermissions.add(permission);
                    }
                } catch (Exception e) {
                    //Don't really care
                }
            }
        }

        if (allPermissions.size() > 1) {
            boolean allGranted = true;
            for (String permission : allPermissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                    allGranted = false;
            }

            if (!allGranted) {
                //TODO An explaination before asking
                requestPermissions(allPermissions.toArray(new String[0]), PERMISSION_REQUESTCODE);
                queueItem = tabs;
                queueMenuItemId = menuItemId;
                return false;
            }

            return true;
        }

        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.settings_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                HolderActivity.startActivity(this, SettingsFragment.class, null);
                return true;
            case R.id.favorites:
                HolderActivity.startActivity(this, FavFragment.class, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // This fires when a notification is opened by tapping on it or one is received while the app is running.

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            BaseFragment childFragment = (BaseFragment) getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);

            if (childFragment.popBackStack()) {
                CLog.e(TAG, getSupportFragmentManager().getFragments().size() + " Stack Count");
                if (getSupportFragmentManager().getFragments().size() == 0)
                    nowTabOpened = false;
                return;
            } else
                nowTabOpened = false;
        }

        Fragment activeFragment = null;
        if (adapter != null)
            activeFragment = adapter.getCurrentFragment();

        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (activeFragment instanceof BackPressFragment) {
            boolean handled = ((BackPressFragment) activeFragment).handleBackPress();
            if (!handled) {
                super.onBackPressed();
            }
        } else if (activeFragment instanceof BaseFragment) {
            boolean handled = ((BaseFragment) activeFragment).popBackStack();
            if (!handled) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null)
            for (Fragment frag : fragments)
                if (frag != null)
                    frag.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!(adapter.getCurrentFragment() instanceof ConfigurationChangeFragment))
            this.recreate();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (adapter == null) return;

        int menuItemIndex = 0;
        for (MenuItem menuItem : menu.getMenuItems()) {
            if (menuItem.isChecked()) {
                menuItemIndex = menuItem.getItemId();
                break;
            }
        }

        outState.putSerializable(STATE_ACTIONS, ((ArrayList<NavItem>) adapter.getActions()));
        outState.putInt(STATE_MENU_INDEX, menuItemIndex);
        outState.putInt(STATE_PAGER_INDEX, viewPager.getCurrentItem());
    }

    //Check if we should adjust our layouts for tablets
    public boolean useTabletMenu() {
        return (getResources().getBoolean(R.bool.isWideTablet) && Config.TABLET_LAYOUT);
    }

    //Apply the appropiate locks to the drawer
    public void applyDrawerLocks() {
        if (drawer == null) {
            if (Config.HIDE_DRAWER)
                navigationView.setVisibility(View.GONE);
            return;
        }

        if (Config.HIDE_DRAWER) {
            toggle.setDrawerIndicatorEnabled(false);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private void lockAppBar() {
        AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(0);
    }

    private void unlockAppBar() {
        AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        switch (messageEvent.getCode()) {
            case MessageEvent.UPDATE_COINS:
                updateHeader();
                break;
            case MessageEvent.WATCH_ADD:
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
                break;
            case MessageEvent.SHOW_INTERSTITIAL_AD:
                showInterstitial();
                break;
        }
    }

    private void updateHeader() {
        View headerLayout = navigationView.getHeaderView(0); // 0-index header
        if (sharedPreferencesManager.isLoggedIn()) {
            TextView nameView = headerLayout.findViewById(R.id.name);
            TextView pointsView = headerLayout.findViewById(R.id.goldenPoints);
            TextView points = headerLayout.findViewById(R.id.points);
            points.setText(app.getUser().points + "");
            headerLayout.findViewById(R.id.userInfoLayout).setVisibility(View.VISIBLE);
            nameView.setText(app.getUser().name);
            pointsView.setText(app.getUser().goldenPoints + "");
            toolbarGoldenPoints.setText(app.getUser().goldenPoints + "");
        } else {
            headerLayout.findViewById(R.id.userInfoLayout).setVisibility(View.GONE);
            BlurredImageView backgroundView = headerLayout.findViewById(R.id.backgroundView);
            backgroundView.downSampling = 0;
            backgroundView.radius = 0;
        }
    }
}



