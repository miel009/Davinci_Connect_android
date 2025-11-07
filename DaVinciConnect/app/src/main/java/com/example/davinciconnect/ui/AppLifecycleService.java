package com.example.davinciconnect.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;

public class AppLifecycleService extends Service {

    private static final String TAG = "AppLifecycleService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved: App is being closed. Clearing data and signing out.");

        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

        // Clear all shared preferences to reset the app state
        SharedPreferences themePrefs = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);
        themePrefs.edit().clear().apply();

        // Clear any other preferences you might have
        SharedPreferences pinPrefs = getSharedPreferences("PinPrefs", Context.MODE_PRIVATE);
        pinPrefs.edit().clear().apply();

        stopSelf();
        super.onTaskRemoved(rootIntent);
    }
}
