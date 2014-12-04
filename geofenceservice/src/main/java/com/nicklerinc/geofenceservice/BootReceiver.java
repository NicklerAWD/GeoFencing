package com.nicklerinc.geofenceservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by nicolas on 03/12/14.
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            context.startService(new Intent(context, GeoFenceServer.class));
            Log.i(TAG, "Starting Service GeoFenceServer");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
