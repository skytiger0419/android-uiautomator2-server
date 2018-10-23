package com.smartisan.uiautomator2.manage;

import android.bluetooth.BluetoothAdapter;

/**
 * 处理蓝牙链接功能
 */
public class BTManage {

    /**
     * 当前 Android 设备是否支持 Bluetooth
     * @return true：支持 Bluetooth false：不支持 Bluetooth
     */
    public static boolean isBluetoothSupported(){
        return BluetoothAdapter.getDefaultAdapter() != null ? true : false;
    }

    /**
     * 当前 Android 设备的 bluetooth 是否已经开启
     * @return true：Bluetooth 已经开启 false：Bluetooth 未开启
     */
    public static boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.isEnabled();
        }
        return false;
    }

    /**
     * 强制开启当前 Android 设备的 Bluetooth
     * @return
     */
    public static boolean openBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if(bluetoothAdapter.isEnabled()) {
                return true;
            }
            return bluetoothAdapter.enable();
        }
        return false;
    }

    /**
     * 关闭蓝牙
     * @return
     */
    public static boolean closeBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if(bluetoothAdapter.isEnabled()){
                return bluetoothAdapter.disable();
            }
            return true;
        }
        return false;
    }





}
