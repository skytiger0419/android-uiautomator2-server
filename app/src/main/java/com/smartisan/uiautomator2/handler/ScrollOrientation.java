package com.smartisan.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.executorserver.WDStatus;
import com.smartisan.uiautomator2.model.AndroidElement;
import org.json.JSONException;
import java.util.Hashtable;

/**
 * 元素级的scroll处理,滑到顶，滑到尾, 上下左右
 *
 */
public class ScrollOrientation extends BaseCommandHandler {

	@Override
	public AndroidCommandResult execute(final AndroidCommand command) throws JSONException {
		if (!command.isElementCommand()) {
			return getErrorResult("A scrollable view is required for this command.");
		}
		final Hashtable<String, Object> params = command.params();
		final String direction = params.get("direction").toString();
		final int steps = Integer.parseInt(params.get("steps").toString());
		try {
			if (command.isElementCommand()) {
				final AndroidElement element  = command.getElement();
				if (element == null) {
					return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
				}
				if(!element.getBoolAttribute("scrollable")){
                    return getErrorResult("element not support scrollable");
                }
                UiScrollable uiScrollable = new UiScrollable(new UiSelector().resourceId(element.getResourceId()));
                if(direction.equalsIgnoreCase("top")){
                    uiScrollable.scrollToBeginning(100, steps);
                }else if(direction.equalsIgnoreCase("bottom")){
                    uiScrollable.scrollToEnd(100, steps);
                }else{
                    return getErrorResult(direction + " command params not exist");
                }
			}
            return getSuccessResult(true);
		} catch (final UiObjectNotFoundException e) {
			return getErrorResult("element not find" + e.getMessage());
		} catch(final IllegalArgumentException e){
			return getErrorResult("Unknown command params");
		}catch (final Exception e) { // handle NullPointerException
			return getErrorResult("Unknown error: " + e.getMessage());
		}
	}

}
