package com.smartisan.uiautomator2.manage;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.smartisan.uiautomator2.utils.Device;
import com.smartisan.uiautomator2.utils.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import static com.smartisan.uiautomator2.utils.ReflectionUtils.method;
import static com.smartisan.uiautomator2.utils.ReflectionUtils.invoke;
/**
 * 获取电话相关信息值
 * Created by Administrator on 2017/6/28.
 */
public class MTelePhoneManage {
    private static MTelePhoneManage instance;
    private TelephonyManager tm;
    private Context context;

    private MTelePhoneManage(){
        context = Device.getInstance().getContext().getApplicationContext();
        tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static MTelePhoneManage getInstance(){
        if(instance == null){
            instance = new MTelePhoneManage();
        }
        return instance;
    }

    /**
     * 获取imsi信息
     * @return
     */
    public String getIMSI(){
        String imsi = null;
        try {
            imsi = tm.getSubscriberId();
        }catch (Exception e){
            Logger.error(e.getMessage());
        }
        return imsi;
    }

    /**
     * 获取手机服务商信息
     */
    public String getProvidersName() {
        String imsi = getIMSI();
        if(TextUtils.isEmpty(imsi)){
            return null;
        }
        String ProvidersName = null;
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (imsi.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (imsi.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }

    /**
     * 获取电话号码
     */
    public String getNativePhoneNumber() {
        String NativePhoneNumber = null;
        NativePhoneNumber = tm.getLine1Number();
        return NativePhoneNumber;
    }

    public String getIccid(){
        try{
            return tm.getSimSerialNumber();
        }catch (Exception e){
            return null;
        }
    }


    public String getPhoneInfo(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("imei",tm.getDeviceId());
            obj.put("imsi",tm.getSubscriberId());
            obj.put("iccid",tm.getSimSerialNumber());
            obj.put("state",tm.getSimState());
            obj.put("networkOperator",tm.getNetworkOperator());
            obj.put("countryiso",tm.getNetworkCountryIso());
            obj.put("NetworkType",tm.getNetworkType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();

    }

    @Override
    public String  toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSimState = " + tm.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
        sb.append("\nsn = " + Device.getInstance().getSerialNum());
        return  sb.toString();
    }

    /**
     * 从数据库中查找sim卡相关信息
     * @param simid
     */
    public int getPhoneInfoInDB(int simid){
        Uri uri = Uri.parse("content://telephony/siminfo");
        Context context = Device.getInstance().getContext().getApplicationContext();
        Cursor cursor = null;
        ContentResolver contentResolver = context.getContentResolver();
        try{
            cursor = contentResolver.query(uri,
                    new String[]{"_id","sim_id"},"sim_id=?",
                    new String[]{String.valueOf(simid)}, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    return cursor.getInt(cursor.getColumnIndex("_id"));
                }
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return -1;

    }

    /**
     * 获取双卡imsi
     * @param simid
     * @return
     */
    public String getImsi(int simid){
        int subid = getPhoneInfoInDB(simid);
        if(subid == -1){
            return null;
        }
        return (String)invoke(method(tm.getClass(),"getSubscriberId",int.class),tm, subid);
    }

    /**
     * 获取双卡imei
     * @param simid
     * @return
     */
    public String getImei(int simid){
        return (String)invoke(method(tm.getClass(),"getImei",int.class),tm, simid);
    }

    /**
     * 获取手机imei,imsi等相关信息，通过卡槽
     * @param simid
     * @return
     */
    public String getPhoneinfo(int simid){
        JSONObject obj = new JSONObject();
        try {
            obj.put("imei",getImei(simid));
            obj.put("imsi",getImsi(simid));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();

    }





}
