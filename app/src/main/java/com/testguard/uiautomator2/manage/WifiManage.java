package com.testguard.uiautomator2.manage;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import com.testguard.uiautomator2.model.SwitchStatus;
import com.testguard.uiautomator2.utils.Device;
import com.testguard.uiautomator2.utils.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;

/**
 * 处理wifi链接功能
 */
public class WifiManage{

    private static WifiManage instance = null;
    private static WifiManager wfm;

    public enum WifiCipherType {
        WIFICIPHER_WEP,WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    private WifiManage(){
        wfm = (WifiManager) Device.getInstance().getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public static WifiManage getInstance(){
        if(instance == null){
            instance = new WifiManage();
        }
        return instance;
    }

    public SwitchStatus toggle(final boolean setTo) {
        SwitchStatus result = new SwitchStatus();
        int wifiState = wfm.getWifiState();
        Logger.debug(wifiState);
        if(!setTo && wifiState == WIFI_STATE_DISABLED){
            return result;
        }else if(setTo && wifiState == WIFI_STATE_ENABLED){
            return result;
        }
        boolean status = wfm.setWifiEnabled(setTo);
        if (!status) {
            String errorMsg = "Unable to " + (setTo ? "ENABLE" : "DISABLE") + "WIFI";
            result.code = 1;
            result.msg = errorMsg;
            return result;
        }
        // If the WIFI state change is in progress,
        // wait until the TIMEOUT has expired
        final int TIMEOUT = 2000;
        final long then = System.currentTimeMillis();
        long now = then;
        while (isInProgress() || !isSuccessful(setTo) && now - then < TIMEOUT) {
            //WIFI State change is in progress, wait for completion
            SystemClock.sleep(500);
            now = System.currentTimeMillis();
        }
        wifiState = wfm.getWifiState();
        if (isInProgress() || !isSuccessful(setTo)) {
            result.code = 1;
            result.msg = String.format("Changing WIFI State not completed in %s ms", TIMEOUT);
            return result;
        }
        if(setTo){
            result.code = (wifiState == WIFI_STATE_ENABLED) ? 0 : 1;
            result.msg = "true";
        }else{
            result.code = (wifiState == WIFI_STATE_DISABLED) ? 0 : 1;
            result.msg = "false";
        }
        return result;

    }

    public boolean getWifiStatus(){
        return wfm.getWifiState() == WIFI_STATE_ENABLED;
    }

    private boolean  isInProgress() {
        return wfm.getWifiState() == WIFI_STATE_DISABLING || wfm.getWifiState() == WIFI_STATE_ENABLING;
    }

    private boolean isSuccessful(boolean desired) {
        if (isInProgress()) {
            Logger.debug("inprogress");
            return false;
        } else if (desired == false && wfm.getWifiState() == WIFI_STATE_DISABLED) {
            Logger.debug("false");
            return true;
        } else if (desired == true && wfm.getWifiState() == WIFI_STATE_ENABLED) {
            Logger.debug("true");
            return true;
        }
        return false;
    }

    public String getWifiInfo(){
        WifiInfo wifiInfo = wfm.getConnectionInfo();
        if(wifiInfo == null){
            return null;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("bssid",wifiInfo.getBSSID());
            json.put("rssi",wifiInfo.getRssi());
            json.put("ssid",wifiInfo.getSSID());
            json.put("ip",wifiInfo.getIpAddress());
            json.put("linkspeed",wifiInfo.getLinkSpeed());
            json.put("networkid",wifiInfo.getNetworkId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public boolean connect(String hot, String password, int type){
        WifiCipherType wifiCipherType = null;
        if(type == 0){
            wifiCipherType = WifiCipherType.WIFICIPHER_WEP;
        }else if(type == 1){
            wifiCipherType = WifiCipherType.WIFICIPHER_WPA;
        }else if (type==2){
            wifiCipherType = WifiCipherType.WIFICIPHER_NOPASS;
        }else{
            wifiCipherType = WifiCipherType.WIFICIPHER_INVALID;
        }
        WifiConfiguration wifiConfig = createWifiInfo(hot, password, wifiCipherType);
        if (wifiConfig == null) {
            return false;
        }
        WifiConfiguration tempConfig = isExsits(hot);
        if (tempConfig != null) {
            wfm.removeNetwork(tempConfig.networkId);
        }
        int netID = wfm.addNetwork(wifiConfig);
        boolean enabled = wfm.enableNetwork(netID, true);
        boolean connected = wfm.reconnect();
        return connected;
    }

    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wfm.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"") /*&& existingConfig.preSharedKey.equals("\"" + password + "\"")*/) {
                return existingConfig;
            }
        }
        return null;
    }

    private WifiConfiguration createWifiInfo(String SSID, String Password, WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if(Type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }else if(Type == WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\""+Password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }else if(Type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\""+Password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // 此处需要修改否则不能自动重联
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        } else {
            return null;
        }
        return config;
    }

    public boolean disconnect(){
        WifiInfo wifiInfo = wfm.getConnectionInfo();
        if(wifiInfo != null){
            wfm.disableNetwork(wifiInfo.getNetworkId());
            return true;
        }
        return false;
    }

}
