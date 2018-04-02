package com.smartisan.uiautomator2.model;

import android.graphics.Point;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.test.uiautomator.UiDevice;
import android.text.format.Formatter;
import com.smartisan.uiautomator2.ServiceManage.MAudioManage;
import com.smartisan.uiautomator2.ServiceManage.MTelePhoneManage;
import com.smartisan.uiautomator2.core.UiAutomatorBridge;
import com.smartisan.uiautomator2.utils.Device;
import com.smartisan.uiautomator2.utils.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;

/**
 *
 * 设备信息详情
 * Created by Administrator on 2017/6/28.
 */
public class DeviceInfo {
    private int displayWidth;
    private int displayHeight;
    private int displayRotation;
    private int displaySizeDpX;
    private int displaySizeDpY;
    private String cpuApi;
    private String manufacturer;
    private String model;
    private String sw_version;
    private String imei1;
    private String imei2;
    private int sdkInt;
    private String productName;
    private String serial;
    private String sim1; //集成电路号
    private String sim2 = null; //集成电路号
    private boolean naturalOrientation;
    private boolean screenOn;
    private boolean handset;
    private String size;
    private double memTotal; //总运行内存


    public DeviceInfo(){
        sdkInt = android.os.Build.VERSION.SDK_INT;
        UiDevice ud = Device.getInstance().getUiDevice();
        Point p = new Point();
        UiAutomatorBridge.getInstance().getDefaultDisplay().getRealSize(p);
        this.displayWidth = p.x;
        this.displayHeight = p.y;
        this.displayRotation = ud.getDisplayRotation();
        this.productName = ud.getProductName();
        this.naturalOrientation = ud.isNaturalOrientation();
        this.displaySizeDpX = ud.getDisplaySizeDp().x;
        this.displaySizeDpY = ud.getDisplaySizeDp().y;
        this.handset = MAudioManage.getHandSetStatus();
        this.serial = Device.getInstance().getSerialNum();
        this.sw_version = Device.getInstance().getSwVersion();
        this.imei1 = Device.getInstance().getIMEI();
        this.imei2 = MTelePhoneManage.getInstance().getImei(1);
        this.manufacturer = Device.getInstance().getProp("ro.product.manufacturer");
        this.model = Device.getInstance().getProp("ro.product.model");
        this.sim1 = MTelePhoneManage.getInstance().getIMSI();
        this.sim2 = MTelePhoneManage.getInstance().getImsi(1);
        this.size = this.displayWidth + "*" + this.displayHeight;
        this.cpuApi = Device.getInstance().getProp("ro.product.cpu.abi");
        this.memTotal = Device.getInstance().getMemSize();
        try {
            this.screenOn = ud.isScreenOn();
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.error(e.getMessage());
        }
    }
    public String toJsonString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("serial",serial);
            obj.put("handset",handset ? 1 : 0);
            obj.put("sdk_version",sdkInt);
            obj.put("imei1",imei1);
            obj.put("imei2",imei2);
            obj.put("size",size);
            obj.put("sw_version",sw_version);
            obj.put("name",productName);
            obj.put("manufacturer",manufacturer);
            obj.put("model",model);
            obj.put("sdcard",Device.getInstance().getExtStoragePath(true) == null ? 0 : 1);
            obj.put("sim1", sim1 == null ? "0": sim1);
            obj.put("sim2", sim2 == null ? "0": sim2);
            obj.put("mem_size",memTotal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    private String getRomTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(Device.getInstance().getContext(), blockSize * totalBlocks);
    }
}
