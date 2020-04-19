package com.albat.mobachir;

import com.albat.mobachir.drawer.SimpleMenu;

/**
 * This file is part of the Universal template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2017
 */

public class Config {

    /**
     * The Config.json file that defines your app's content.
     * Point url to JSON or leave empty to use config.json from assets.
     */


    public static final String CONFIG_URL = "http://nowfoot.com/configapp.json";
    public static final String API_URL = "http://nowfoot.com/admin/api/";

    public static final String TWITTER_CONSUMER_KEY = "wZhiwX2LoYGlTrc3K8Lu5IDsu";
    public static final String TWITTER_CONSUMER_SECRET = "GnUtZcpS7WHNtQY5JQwspQT7aBSiojtqpd4Hhp0UV9GXAQCfdp";
    /**
     * Options regarding the link behaviour in your app
     */

    //Explicit links, like 'open' buttons, should be opened outside the app
    public static final boolean OPEN_EXPLICIT_EXTERNAL = true;
    //Inline links, like links in descriptions, should be opened outside the app
    public static final boolean OPEN_INLINE_EXTERNAL = false;

    /**
     * Options related to how your app looks
     */

    //Show category chips in WordPress
    public static final boolean WP_CHIPS = true;
    //Use immersive row layout with WordPress
    public static final boolean WP_ROW_IMMERSIVE = false;
    //Show a attachments fab for WordPress posts
    public static final boolean WP_ATTACHMENTS_BUTTON = true;

    //Show category chips in WooCommerce
    public static final boolean WC_CHIPS = true;

    //Use immersive row layout with Youtube
    public static final boolean YT_ROW_IMMERSIVE = true;

    //Hide the navigation drawer
    public static final boolean HIDE_DRAWER = false;
    //If a tablet layout should be used on tablets, or just a regular layout
    public static final boolean TABLET_LAYOUT = true;
    //Force show menu on app start
    public static final boolean DRAWER_OPEN_START = false;

    //If the WebView navigation buttons should be hidden
    public static final boolean FORCE_HIDE_NAVIGATION = true;

    //If a visualiser should be shown for the Radio player
    public static boolean VISUALIZER_ENABLED = true;
    //If the visualiser is not show, a background image resource id
    public static int BACKGROUND_IMAGE_ID = R.drawable.radio_background;

    /**
     * Options related to Advertisements in your app
     */

    //The frequency in which interstitial ads are shown
    //('0' to never show, '1' to always show, '2' to show 1 out of 2, etc)
    public static final int INTERSTITIAL_INTERVAL = 3;
    //If ads are enabled, also show them on the youtube layout
    public static final boolean ADMOB_YOUTUBE = false;

    /**
     * Options regarding the use of a Hardcoded Configuration
     */

    //Will load configuration from hardcoded Config class instead of JSON.
    public static boolean USE_HARDCODED_CONFIG = false;

    //If you use a hardcoded config, initialise it below
    public static void configureMenu(SimpleMenu menu, ConfigParser.CallBack callback) {

        /**
         List<NavItem> firstTabs = new ArrayList<NavItem>();
         firstTabs.add(new NavItem("RSS", RssFragment.class,
         new String[]{"http://hihi2.com/feed"}));
         firstTabs.add(new NavItem("Rss Podcast", RssFragment.class,
         new String[]{"http://feeds.nature.com/nature/podcast/current"}));
         firstTabs.add(new NavItem("SoundCloud", SoundCloudFragment.class,
         new String[]{"13568105"}));
         firstTabs.add(new NavItem("WebView", WebviewFragment.class,
         new String[]{"http://sherdle.com"}));
         menu.add("Other", R.drawable.ic_details, firstTabs);

         List<NavItem> blogTabs = new ArrayList<NavItem>();
         blogTabs.add(new NavItem("Jetpack", WordpressFragment.class,
         new String[]{"en.blog.wordpress.com", ""}));
         blogTabs.add(new NavItem("Jetpack Cat", WordpressFragment.class,
         new String[]{"en.blog.wordpress.com", "events"}));
         blogTabs.add(new NavItem("Wordpress Recent", WordpressFragment.class,
         new String[]{"http://androidpolice.com", "", "http://androidpolice.disqus.com/;androidpolice;%d http://www.androidpolice.com/?p=%d"}));
         menu.add("Blogs", R.drawable.ic_details, blogTabs);

         List<NavItem> streamingTabs = new ArrayList<>();
         streamingTabs.add(new NavItem("Video", TvFragment.class,
         new String[]{"http://abclive.abcnews.com/i/abc_live4@136330/index_1200_av-b.m3u8"}));
         streamingTabs.add(new NavItem("AAC Shoutcast", RadioFragment.class,
         new String[]{"http://yp.shoutcast.com/sbin/tunein-station.pls?id=830692", "visualizer"}));
         streamingTabs.add(new NavItem("3FM", RadioFragment.class,
         new String[]{"http://yp.shoutcast.com/sbin/tunein-station.m3u?id=709809", "visualizer"}));
         menu.add("Streaming", R.drawable.ic_details, streamingTabs, true);

         SimpleSubMenu sub = new SimpleSubMenu(menu, "Test");
         List<NavItem> thirdTabs = new ArrayList<NavItem>();
         thirdTabs.add(new NavItem("Twitter", TweetsFragment.class,
         new String[]{"Android"}));
         thirdTabs.add(new NavItem("SoundCloud", SoundCloudFragment.class,
         new String[]{"13568105"}));
         thirdTabs.add(new NavItem("Tumblr", TumblrFragment.class,
         new String[]{"androidbackgrounds"}));
         thirdTabs.add(new NavItem("Instagram", InstagramFragment.class,
         new String[]{"2948597263"}));
         thirdTabs.add(new NavItem("Facebook", FacebookFragment.class,
         new String[]{"104958162837"}));
         thirdTabs.add(new NavItem("Youtube Channel", YoutubeFragment.class,
         new String[]{"PLOcMSsuppV4pWBxVVJGE9dOeHUtOxHJDd","UC7V6hW6xqPAiUfataAZZtWA"}));
         thirdTabs.add(new NavItem("Youtube PlayList", YoutubeFragment.class,
         new String[]{"PLOcMSsuppV4pWBxVVJGE9dOeHUtOxHJDd"}));
         sub.add("Social", R.drawable.ic_details, thirdTabs, true);

         List<NavItem> permissionTabs = new ArrayList<>();
         permissionTabs.add(new NavItem("AAC Shoutcast", RadioFragment.class,
         new String[]{"http://yp.shoutcast.com/sbin/tunein-station.pls?id=830692", "visualizer"}));
         permissionTabs.add(new NavItem("Maps Query", MapsFragment.class,
         new String[]{"pharmacy"}));
         permissionTabs.add(new NavItem("Maps Point", MapsFragment.class,
         new String[]{"<b>Adress:</b><br>SomeStreet 5<br>Sydney, Australia<br><br><i>Email: Mail@Company.com</i>",
         "Company",
         "This is where our office is.",
         "-33.864",
         "151.206",
         "13"}));
         sub.add("Permissions", R.drawable.ic_details, permissionTabs);

         List<NavItem> customTab = new ArrayList<>();
         customTab.add(new NavItem("Open App", CustomIntent.class,
         new String[]{ "com.spotify.music", CustomIntent.OPEN_APP}));
         sub.add("Custom", R.drawable.ic_details, customTab);

         **/

        //Return the configuration
        callback.configLoaded(false);
    }

    public enum LoginType {
        NORMAL(0),
        FACEBOOK(1),
        TWITTER(2),
        GOOGLE_PLUS(3);

        private int value;

        LoginType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public interface FixtureGuessStatus {
        int DRAW = 0;
        int TEAM1WIN = 1;
        int TEAM2WIN = 2;
    }

    public static final String API_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String API_DATE_FORMAT = "yyyy-MM-dd";
    public static final String APP_DATETIME_FORMAT = "d/M hh:mm a";
    public static final String APP_DATE_FORMAT = "M/d";
    public static final String APP_FULL_DATE_FORMAT = "M/d/yyyy";
    public static final String APP_TIME_FORMAT = "hh:mm a";

    public static final int MAX_COMMENT_CHAR = 180;


    public static final String SHARE_TEXT = "تطبيق البث المباشر لتغطية جميع البطولات و نتائج المباريات و بثها مباشرة";
    public static final String GOOGLE_PLAY_APP_URL = "https://play.google.com/store/apps/details?id=";
}