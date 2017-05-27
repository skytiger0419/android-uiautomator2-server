package com.yep.uiautomator2.handler;

import android.graphics.Rect;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.text.TextUtils;

import com.yep.uiautomator2.common.exceptions.InvalidCoordinatesException;
import com.yep.uiautomator2.core.UiAutomatorBridge;
import com.yep.uiautomator2.executorserver.AndroidCommand;
import com.yep.uiautomator2.executorserver.AndroidCommandResult;
import com.yep.uiautomator2.executorserver.WDStatus;
import com.yep.uiautomator2.model.AndroidElement;
import com.yep.uiautomator2.utils.Logger;
import com.yep.uiautomator2.utils.Point;
import com.yep.uiautomator2.utils.PositionHelper;

import org.json.JSONException;

import java.util.Hashtable;

/**长按事件
 * Created by Administrator on 2017/3/12.
 */
public class TouchLongClick extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            final Hashtable<String, Object> params = command.params();
            if(command.isElementCommand()){
                AndroidElement element = command.getElement();
                if (element == null) {
                    return new AndroidCommandResult( WDStatus.NO_SUCH_ELEMENT);
                }
                if(!params.containsKey("duration")){
                    element.longClick();
                }else{
                    int duration = Integer.parseInt(params.get("duration").toString());
                    Rect bounds = element.getBounds();
                    if(longPress(bounds.centerX(),bounds.centerY(),duration)){
                        getSuccessResult(true);
                    }
                }
            }else{
                Point coords = new Point(Double.parseDouble(params.get("x").toString()),
                        Double.parseDouble(params.get("y").toString()));
                coords = PositionHelper.getDeviceAbsPos(coords);
                int duration = Integer.parseInt(params.get("duration").toString());
                if(longPress(coords.x.intValue(),coords.y.intValue(), duration)){
                    getSuccessResult(true);
                }
            }

        } catch (UiObjectNotFoundException e) {
            return new AndroidCommandResult( WDStatus.NO_SUCH_ELEMENT);
        } catch (InvalidCoordinatesException e) {
            return new AndroidCommandResult( WDStatus.INVALID_ELEMENT_COORDINATES,"not invalid coordinate");
        }
        return getSuccessResult("Long Click action performed");
    }

    private boolean longPress(int x, int y, int duration){
        boolean flag1 = UiAutomatorBridge.getInstance().getInteractionController().touchDown(x,y);
        try {
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean flag2 = UiAutomatorBridge.getInstance().getInteractionController().touchUp(x,y);
        return flag1 && flag2;
    }
}
