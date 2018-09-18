package eokoe.teste.developer.lln.movielist.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class UtilsNoInternet {

    static boolean isConnectedToWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiInfo.isConnected();
    }

    public static void turnOn3g(Context context) {
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    public static void turnOnWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager == null || isConnectedToWifi(context)) {
            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            return;
        }

        wifiManager.setWifiEnabled(true);
    }
}
