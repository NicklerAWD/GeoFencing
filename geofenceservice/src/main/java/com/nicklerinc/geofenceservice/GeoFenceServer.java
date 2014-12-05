package com.nicklerinc.geofenceservice;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 03/12/14.
 */
public class GeoFenceServer extends Service implements LocationListener {

    private String TAG = "GeoFenceServer";
    private LocationManager mLocationManager = null;
    private String TAG2 = "GeoLocationListener";
    private List<GeoFencePoint> ListGFP = null;
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    GeoFencePoint mValue = null;
    int GFPindex = 0;
    //Config
    private int MAX_GEOFENCES = 2;
    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int ADD_GEOFENCE = 3;
    static final int REMOVE_GEOFENCE = 4;
    static final int GET_GEOFENCES_LIST = 5;
    static final int GEOFENCES_LIST_BACK = 6;

    private float CheckLat = 0;
    private float CheckLongi = 0;
    private float CheckRadius = 0;
    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case ADD_GEOFENCE:
                    mValue = (GeoFencePoint)msg.obj;
                    addGeoFencePoint(mValue);
                    break;
                case REMOVE_GEOFENCE:
                    GFPindex = msg.arg1;
                    RemoveGeoFencePoint(GFPindex);
                    break;
                case GET_GEOFENCES_LIST:
                    try {
                        msg.replyTo.send(Message.obtain(null,GEOFENCES_LIST_BACK,GetGeoFencePoint()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service Started");
        //Assign the location service for the location
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);
        //mLocationManager.addGpsStatusListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service stopped");
        mLocationManager.removeUpdates(this);
    }

    //Method for handling the GeoFencePoint

    /**
     *
     * @param GFP
     * @return
     */
    public boolean addGeoFencePoint(GeoFencePoint GFP){
        if(ListGFP == null){
            ListGFP = new ArrayList<GeoFencePoint>();
        }
        if(ListGFP.size() == MAX_GEOFENCES){
            Log.w(TAG,"List is full. Please Remove one");
            return false;
        }
        ListGFP.add(GFP);
        return true;
    }

    /**
     *
     * @param index
     * @return
     */
    public boolean RemoveGeoFencePoint(int index){
        if(ListGFP == null){
            return false;
        }
        ListGFP.remove(index);
        if(ListGFP.size() == 0){
            ListGFP = null;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public List<GeoFencePoint> GetGeoFencePoint(){
        return ListGFP;
    }


    /**
     *
     * @param loc
     */
    @Override
    public void onLocationChanged(Location loc) {
        if (ListGFP != null) {
            IsInCircle(loc.getLongitude(),loc.getLatitude(),loc.getAccuracy());
        }
    }

    /**
     *
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {
    }

    /**
     *
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {
    }

    /**
     *
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    /**
     *
     * @return
     */
    private boolean IsInCircle(double CurrentLatitude, double Currentlongitude, double CurrentAccuracy ) {
        for (GeoFencePoint element : ListGFP){
            CheckLat = element.GetLatitude();
            CheckLongi = element.GetLongitude();
            CheckRadius = element.GetRadius();

            if((CheckRadius*CheckRadius) <= (float)Math.pow(CheckLat-(float)CurrentLatitude,2) + (float)Math.pow(CheckLongi-(float)Currentlongitude,2)){
                return true;
            }
        }
        return false;
    }
}
