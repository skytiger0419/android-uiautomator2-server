package com.testguard.uiautomator2.handler;


import android.os.Bundle;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Hashtable;

public class Find extends BaseCommandHandler {
  public static Bundle params            = null;
  static JSONObject apkStrings           = null;

  @Override
  public AndroidCommandResult execute(final AndroidCommand command)
      throws JSONException {
    return execute(command, false);
  }

  public AndroidCommandResult execute( AndroidCommand command, boolean isRetry){
    try {
        final Hashtable<String, Object> params = command.params();
        final boolean multiple = (Boolean) params.get("multiple");
        if(multiple){
          return new FindElements().execute(command);
        }else{
          return new FindElement().execute(command);
        }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return getErrorResult("command parse error");
  }

}
