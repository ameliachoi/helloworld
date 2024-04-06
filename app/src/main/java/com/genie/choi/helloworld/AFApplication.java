package com.genie.choi.helloworld;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;
import android.app.Application;

import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerConversionListener;

import java.util.Map;
import java.util.Objects;
import android.util.Log;

import androidx.annotation.NonNull;

public class AFApplication extends Application {

    private static final String AF_DEV_KEY = "pxVVEjj4StfGiAKPjBUkyM";

    @Override
    public void onCreate() {
        super.onCreate();

        AppsFlyerLib appsflyer = AppsFlyerLib.getInstance();

        appsflyer.setDebugLog(true);
        appsflyer.setMinTimeBetweenSessions(0);

        appsflyer.init(AF_DEV_KEY, null, this);
        appsflyer.start(this);

        Map<String, Object> eventValues = new HashMap<String, Object>();
        eventValues.put(AFInAppEventParameterName.CURRENCY, "USD");
        eventValues.put(AFInAppEventParameterName.REVENUE, 2);
        AppsFlyerLib.getInstance().logEvent(getApplicationContext(), AFInAppEventType.PURCHASE, eventValues);

    }
}
