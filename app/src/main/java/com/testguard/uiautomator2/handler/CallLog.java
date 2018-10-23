package com.testguard.uiautomator2.handler;

import com.testguard.uiautomator2.manage.CallLogManage;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;
import java.util.Hashtable;

/**
 * 通话记录生成与清空
 * Created by Administrator on 2017/6/9.
 */
public class CallLog extends BaseCommandHandler{
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try{
            Hashtable<String, Object> paramas = command.params();
            String type = (String)paramas.get("type");
            if(type.equalsIgnoreCase("add")){
                String phone = (String)paramas.get("phone");
                int size = (Integer)paramas.get("num");
                CallLogManage.makeCallLog(phone, size);
                return getSuccessResult(true);
            }if(type.equalsIgnoreCase("clear")){
                CallLogManage.clearAll();
                return getSuccessResult(true);
            }
            return getErrorResult("contact not support commnad type");
        }catch (Exception e){
            return getErrorResult(e.getMessage());
        }
    }
}
