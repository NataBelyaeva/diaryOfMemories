package com.example.diary;


import android.app.Application;
import android.util.Log;

import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("MapKit", "API Key: " + BuildConfig.MAPKIT_API_KEY);
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
    }
}
