package com.yep.uiautomator2.ServiceManage;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import com.yep.uiautomator2.executorserver.AndroidCommandResult;
import com.yep.uiautomator2.executorserver.WDStatus;
import com.yep.uiautomator2.utils.Device;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;

/**
 * 处理wifi链接功能
 */
public class WifiHandler{

    private static WifiManager wfm;

    public static AndroidCommandResult toggle(final boolean setTo) {
        wfm = (WifiManager) Device.getInstance().getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean status = wfm.setWifiEnabled(setTo);
        if (!status) {
            String errorMsg = "Unable to " + (setTo ? "ENABLE" : "DISABLE") + "WIFI";
            return new AndroidCommandResult( WDStatus.UNKNOWN_ERROR, errorMsg);
        }
        int wifiState = wfm.getWifiState();
        // If the WIFI state change is in progress,
        // wait until the TIMEOUT has expired
        final int TIMEOUT = 2000;
        final long then = System.currentTimeMillis();
        long now = then;
        while (isInProgress() || !isSuccessful(setTo) && now - then < TIMEOUT) {
            //WIFI State change is in progress, wait for completion
            SystemClock.sleep(100);
            now = System.currentTimeMillis();
            wifiState = wfm.getWifiState();
        }
        if (isInProgress() || !isSuccessful(setTo)) {
            return new AndroidCommandResult(WDStatus.TIMEOUT
                    , String.format("Changing WIFI State not completed in %s ms", TIMEOUT));
        }
        int response = (wifiState == WIFI_STATE_ENABLED) ? 1 : 0;
        return new AndroidCommandResult(WDStatus.SUCCESS, response);

    }

    private static boolean  isInProgress() {
        return wfm.getWifiState() == WIFI_STATE_DISABLING || wfm.getWifiState() == WIFI_STATE_ENABLING;
    }

    private static boolean isSuccessful(boolean desired) {
        if (isInProgress()) {
            return false;
        } else if (desired == false && wfm.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            return true;
        } else if (desired == true && wfm.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            return true;
        }
        return false;
    }

}
