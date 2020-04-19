package com.albat.mobachir.network.interfaces;

import com.albat.mobachir.network.models.AppVersion;
import com.albat.mobachir.network.models.Stage;

import java.util.ArrayList;

public interface AppVersionRetrieved {
    void onAppVersionRetrieved(AppVersion appVersion, boolean success, String error);
}
