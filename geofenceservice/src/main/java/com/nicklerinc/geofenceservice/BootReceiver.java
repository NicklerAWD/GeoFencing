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
            if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
                Log.i(TAG, "Starting Service GeoFenceServer");
                context.startService(new Intent(context, GeoFenceServer.class));
            }else if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")) {
                Log.d(TAG, "Shutdown Complete");
                context.stopService(new Intent(context, GeoFenceServer.class));
            }else if (intent.getAction().equals("com.nicklerinc.geofenceservice.StopGeoFencingService")) {
                Log.d(TAG, "Stopping the service");
                context.stopService(new Intent(context, GeoFenceServer.class));
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
