package com.testguard.uiautomator2.handler;

import android.app.AlarmManager;
import android.content.Context;

import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.utils.Device;

import org.json.JSONException;
import java.util.Hashtable;
import java.util.TimeZone;

/**
 * 修改手机默认时区
 */
public class TimeZoneHandler extends BaseCommandHandler {


    @Override
    public AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException {
        final Hashtable<String, Object> params = command.params();
        final String target = (String) params.get("target");
        Context context = Device.getInstance().getContext();
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        TimeZone tz = TimeZone.getDefault();
        if(tz.getID().equalsIgnoreCase(target)){
            return getSuccessResult("time zone is " + target);
        }else{
            alarmManager.setTimeZone(target);
            TimeZone tz2 = TimeZone.getDefault();
            if(tz2.getID().equalsIgnoreCase(target)){
                return getSuccessResult("time zone modify to " + target);
            }else{
                return getErrorResult("time zone modify failure");
            }
        }
    }
}
