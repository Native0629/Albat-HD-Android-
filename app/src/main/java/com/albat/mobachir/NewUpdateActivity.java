package com.albat.mobachir;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.albat.mobachir.network.models.AppVersion;
import com.albat.mobachir.util.IntentLaunchers;


public class NewUpdateActivity extends BaseActivity {
    public static final String APP_VERSION = "APP_VERSION";
    Button updateNow;
    TextView info;

    AppVersion appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_update);

        appVersion = (AppVersion) getIntent().getSerializableExtra(APP_VERSION);

        initializeViews();
    }

    private void initializeViews() {

        updateNow = (Button) findViewById(R.id.updateNow);
        info = (TextView) findViewById(R.id.info);


        updateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentLaunchers.openBrowserActivity(NewUpdateActivity.this, appVersion.appUrl);
            }
        });

    }
}
