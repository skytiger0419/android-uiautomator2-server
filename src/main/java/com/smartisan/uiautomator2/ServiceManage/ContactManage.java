package com.smartisan.uiautomator2.ServiceManage;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import com.smartisan.uiautomator2.utils.Device;
import com.smartisan.uiautomator2.utils.Logger;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import static android.provider.ContactsContract.Directory.ACCOUNT_NAME;
import static android.provider.ContactsContract.Directory.ACCOUNT_TYPE;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName;
import java.util.ArrayList;

/**
 * 联系人管理
 * Created by Administrator on 2017/6/9.
 */
public class ContactManage {

    public static void makeContacts(String startNum, int size, String name) {
        Context context = Device.getInstance().getContext().getApplicationContext();
        ContentResolver resolver = context.getContentResolver();
        final ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = 0;
        for (int i = 0; i < size; i++) {
            rawContactInsertIndex = operationList.size();
            Long tempPhone = Long.parseLong(startNum) + i;
            operationList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(RawContacts.ACCOUNT_NAME, ACCOUNT_NAME)
                    .withValue(RawContacts.ACCOUNT_TYPE, ACCOUNT_TYPE)
                    .withYieldAllowed(true).build());
            String tempName = name;
            if(TextUtils.isEmpty(tempName)){
                tempName = tempPhone+"";
            }else{
                if(size != 1){
                    tempName = tempName + i;
                }
            }

            // 添加姓名
            operationList.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(StructuredName.DISPLAY_NAME, tempName).build());
            // 添加号码
            operationList.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Phone.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                    .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                    .withValue(Phone.NUMBER, tempPhone).withValue(Data.IS_PRIMARY, 1)
                    .withYieldAllowed(true).build());
        }
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operationList);
        } catch (OperationApplicationException e) {
            Logger.error(String.format("%s: %s", e.toString(), e.getMessage()));
        } catch (RemoteException e) {
            Logger.error(String.format("%s: %s", e.toString(), e.getMessage()));
        } catch (Exception e) {
            Logger.error(String.format("%s: %s", e.toString(), e.getMessage()));
        }
    }

    /**
     * 清理所有联系人
     */
    public static void clearAll(){
        Context context = Device.getInstance().getContext().getApplicationContext();
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(RawContacts.CONTENT_URI, null, null);
    }

    /**
     * 查看目录前联系人个数
     */
    public static int getContancsNum(){
        Context context = Device.getInstance().getContext().getApplicationContext();
        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._COUNT}, null, null, null);
        try{
            c.moveToFirst();
            return c.getInt(0);
        }catch(Exception e){
            return 0;
        }finally{
            c.close();
        }
    }


}
