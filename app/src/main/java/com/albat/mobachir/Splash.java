package com.albat.mobachir;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.albat.mobachir.network.interfaces.AppVersionRetrieved;
import com.albat.mobachir.network.interfaces.UserLoggedIn;
import com.albat.mobachir.network.managers.UserManager;
import com.albat.mobachir.network.models.AppVersion;
import com.albat.mobachir.network.models.User;
import com.albat.mobachir.util.SharedPreferencesManager;

public class Splash extends AppCompatActivity implements UserLoggedIn, AppVersionRetrieved {
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.splash_primaryDarkColor));
        }

        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.imageView);


        Thread timer = new Thread() {

            @Override
            public void run() {

                try {
                    sleep(500);
                    getAppVersion();
                    super.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };

        timer.start();
    }

    private void getAppVersion() {
        UserManager userManager = new UserManager();
        userManager.getAppVersion(this);
    }

    @Override
    public void onAppVersionRetrieved(AppVersion appVersion, boolean success, String error) {
        if (!success || appVersion == null) {
            if (sharedPreferencesManager.isLoggedIn())
                getUser();
            else
                openMainActivity();
            return;
        }

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            if (!appVersion.appVersion.equalsIgnoreCase(version)) {
                Intent intent = new Intent(this, NewUpdateActivity.class);
                intent.putExtra(NewUpdateActivity.APP_VERSION, appVersion);
                startActivity(intent);
                finish();
                return;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }

        if (sharedPreferencesManager.isLoggedIn())
            getUser();
        else
            openMainActivity();
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getUser() {
        UserManager userManager = new UserManager();
        userManager.getUser(this);
    }

    @Override
    public void onUserLoggedIn(User user, boolean success, String error) {
        if (!success || user == null) {
            openMainActivity();
            return;
        }

        sharedPreferencesManager.saveUser(user);
        App.getInstance().setUser(user);

        openMainActivity();
    }
}
