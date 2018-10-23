package com.smartisan.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.executorserver.WDStatus;
import com.smartisan.uiautomator2.model.AndroidElement;
import com.smartisan.uiautomator2.utils.Logger;
import org.json.JSONException;
import java.util.Hashtable;

/**
 * 元素级的swipe处理
 */
public class SwipeOrientation extends BaseCommandHandler {

	@Override
	public AndroidCommandResult execute(final AndroidCommand command) throws JSONException {
		final Hashtable<String, Object> params = command.params();
		final String direction = params.get("direction").toString();
		final int steps = Integer.parseInt(params.get("steps").toString());
		try {
			if (command.isElementCommand()) {
				final AndroidElement element  = command.getElement();
				if (element == null) {
					return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
				}
				Logger.debug("Swiping " + direction + " % " + "with steps: " + steps);
				return getSuccessResult(element.SwipeOrientation(direction, steps));
			}else{
				return getErrorResult("not suppport non element command");
			}
		} catch (final UiObjectNotFoundException e) {
			return getErrorResult("element not find" + e.getMessage());
		} catch(final IllegalArgumentException e){
			return getErrorResult("Unknown command params");
		}catch (final Exception e) { // handle NullPointerException
			return getErrorResult("Unknown error: " + e.getMessage());
		}
	}
}
