package com.smartisan.uiautomator2.handler;

import com.smartisan.uiautomator2.manage.ContactManage;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Hashtable;

/**
 * 操作手机联系人
 * Created by Administrator on 2017/6/9.
 */
public class Contact extends BaseCommandHandler{
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            Hashtable<String, Object> paramas = command.params();
            String type = (String)paramas.get("type");
            if(type.equalsIgnoreCase("add")){
                String name = null;
                String phone = (String)paramas.get("phone");
                int size = (Integer)paramas.get("num");
                Object obj = paramas.get("name");
                if (obj != null && obj != JSONObject.NULL){
                    name = (String)obj;
                }
                ContactManage.makeContacts(phone, size, name);
                return getSuccessResult(true);
            }if(type.equalsIgnoreCase("clear")){
                ContactManage.clearAll();
                return getSuccessResult(true);
            }if(type.equalsIgnoreCase("get")){
                return getSuccessResult(ContactManage.getContancsNum());
            }
            return getErrorResult("contact not support commnad type");
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
    }
}
