package com.testguard.uiautomator2.manage;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.CallLog;
import com.testguard.uiautomator2.utils.Device;
import com.testguard.uiautomator2.utils.Logger;
import java.util.ArrayList;

/**
 * 通话记录管理
 * Created by Administrator on 2017/6/9.
 */
public class CallLogManage {

    public static void makeCallLog(String targetNum, int size){
        Context context = Device.getInstance().getContext().getApplicationContext();
        ContentResolver resolver = context.getContentResolver();
        final ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
        ContentValues values = new ContentValues();
        for (int i = 0; i < size; i++){
            Long tempPhone = Long.parseLong(targetNum) + i;
            values.clear();
            values.put(CallLog.Calls.NUMBER, tempPhone);
            values.put(CallLog.Calls.TYPE, 1);
            values.put(CallLog.Calls.DATE, System.currentTimeMillis());
            values.put(CallLog.Calls.DURATION, 100);
            values.put(CallLog.Calls.NEW, "0");// 0已看1未看 ,由于没有获取默认全为已读
            operationList.add(ContentProviderOperation
                    .newInsert(CallLog.Calls.CONTENT_URI).withValues(values)
                    .withYieldAllowed(true).build());
        }
        try {
            resolver.applyBatch(CallLog.AUTHORITY, operationList);
        } catch (OperationApplicationException e) {
            Logger.error(String.format("%s: %s", e.toString(), e.getMessage()));
        } catch (RemoteException e) {
            Logger.error(String.format("%s: %s", e.toString(), e.getMessage()));
        }catch (Exception e) {
            Logger.error(String.format("%s: %s", e.toString(), e.getMessage()));
        }
    }

    public static void clearAll(){
        Context context = Device.getInstance().getContext().getApplicationContext();
        ContentResolver resolver = context.getContentResolver();
        try{
            resolver.delete(CallLog.Calls.CONTENT_URI, null, null);
        }catch (Exception e){

        }
    }
}
