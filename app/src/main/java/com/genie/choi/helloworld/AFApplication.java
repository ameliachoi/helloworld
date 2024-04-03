package com.genie.choi.helloworld;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;
import android.app.Application;

import com.appsflyer.AppsFlyerLib;
import com.appsflyer.deeplink.DeepLink;
import com.appsflyer.deeplink.DeepLinkListener;
import com.appsflyer.deeplink.DeepLinkResult;
import com.appsflyer.AppsFlyerConversionListener;

import java.util.Map;
import java.util.Objects;
import android.util.Log;

import androidx.annotation.NonNull;

public class AFApplication extends Application {

    public static final String LOG_TAG = "AppsFlyerOneLinkSimApp";
    private static final String AF_DEV_KEY = "pxVVEjj4StfGiAKPjBUkyM";

    @Override
    public void onCreate() {
        super.onCreate();

        AppsFlyerLib appsflyer = AppsFlyerLib.getInstance();

        appsflyer.setDebugLog(true);
        appsflyer.setMinTimeBetweenSessions(0);

        appsflyer.subscribeForDeepLink(new DeepLinkListener() {
            @Override
            public void onDeepLinking(@NonNull DeepLinkResult deepLinkResult) {
                DeepLinkResult.Status dlStatus = deepLinkResult.getStatus();
                if (dlStatus == DeepLinkResult.Status.FOUND) {
                    Log.d(LOG_TAG, "Deep link found");
                } else if (dlStatus == DeepLinkResult.Status.NOT_FOUND) {
                    Log.d(LOG_TAG, "Deep link not found");
                    return;
                } else {
                    DeepLinkResult.Error dlError = deepLinkResult.getError();
                    Log.d(LOG_TAG, "There was an error getting deep Link data: " + dlError.toString());
                    return;
                }
                DeepLink deepLinkObj = deepLinkResult.getDeepLink();
                try {
                    Log.d(LOG_TAG, "The deeplink data is: " + deepLinkObj.toString());
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Deeplink data came back null");
                    return;
                }

                /* deferred deep link*/
                if (deepLinkObj.isDeferred()) {
                    Log.d(LOG_TAG, "This is a deferred deep link");
                } else {
                    Log.d(LOG_TAG, "This is a direct deep link");
                }
            }
    });

        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionDataMap) {
                for (String attrName: conversionDataMap.keySet())
                    Log.d(LOG_TAG, "Conversion attribute: " + attrName + "=" + conversionDataMap.get(attrName));
                String status = Objects.requireNonNull(conversionDataMap.get("af_status")).toString();
                if(status.equals("Non-organic")) {
                    if (Objects.requireNonNull(conversionDataMap.get("is_first_launch")).toString().equals("true")) {
                        Log.d(LOG_TAG, "Conversion: First Launch");
                    } else {
                        Log.d(LOG_TAG, "Conversion: Not First Launch");
                    }
                } else {
                    Log.d(LOG_TAG, "Conversion: This is an organic install.");
                }
                conversionDataMap = conversionDataMap;
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d(LOG_TAG, "error getting conversion data: " + errorMessage);
            }

            /** deep linking **/

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {
                Log.d(LOG_TAG, "onAppOpenAttribution: This is fake call.");
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d(LOG_TAG, "error onAttributionFailure: " + errorMessage);
            }
        };

        appsflyer.init(AF_DEV_KEY, null, this);
        appsflyer.start(this);

    }
}
