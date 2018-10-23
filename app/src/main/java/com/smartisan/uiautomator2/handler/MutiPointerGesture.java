package com.smartisan.uiautomator2.handler;

import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import com.smartisan.uiautomator2.core.UiAutomatorBridge;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.executorserver.WDStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static com.smartisan.uiautomator2.utils.API.API_18;

/**
 * 多点触控
 * Created by Administrator on 2017/3/12.
 */

public class MutiPointerGesture extends BaseCommandHandler {

    @Override
    public AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException {
        try {
            final PointerCoords[][] pcs = parsePointerCoords(command);
            if (API_18) {
                Boolean rt = UiAutomatorBridge.getInstance().getInteractionController().performMultiPointerGesture(pcs);
                if (rt) {
                    return getSuccessResult("OK");
                } else {
                    return getErrorResult("Unable to perform multi pointer gesture");
                }
            } else {
                return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR,
                        "Cannot perform multi pointer gesture on device below API level 18");
            }
        } catch (final Exception e) {
            return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR, e.getMessage());
        }
    }

    private double computeLongestTime(final JSONArray actions)
            throws JSONException {
        double max = 0.0;
        for (int i = 0; i < actions.length(); i++) {
            final JSONArray gestures = actions.getJSONArray(i);
            final double endTime = gestures.getJSONObject(gestures.length() - 1)
                    .getDouble("time");
            if (endTime > max) {
                max = endTime;
            }
        }

        return max;
    }

    private PointerCoords createPointerCoords(final JSONObject obj)
            throws JSONException {
        final JSONObject o = obj.getJSONObject("touch");

        final int x = o.getInt("x");
        final int y = o.getInt("y");

        final PointerCoords p = new PointerCoords();
        p.size = 1;
        p.pressure = 1;
        p.x = x;
        p.y = y;

        return p;
    }


    private PointerCoords[] gesturesToPointerCoords(final double maxTime,
                                                    final JSONArray gestures) throws JSONException {
        // gestures, e.g.:
        // [
        // {"touch":{"y":529.5,"x":120},"time":0.2},
        // {"touch":{"y":529.5,"x":130},"time":0.4},
        // {"touch":{"y":454.5,"x":140},"time":0.6},
        // {"touch":{"y":304.5,"x":150},"time":0.8}
        // ]

        // From the docs:
        // "Steps are injected about 5 milliseconds apart, so 100 steps may take
        // around 0.5 seconds to complete."
        final int steps = (int) (maxTime * 200) + 2;

        final PointerCoords[] pc = new MotionEvent.PointerCoords[steps];

        int i = 1;
        JSONObject current = gestures.getJSONObject(0);
        double currentTime = current.getDouble("time");
        double runningTime = 0.0;
        final int gesturesLength = gestures.length();
        for (int j = 0; j < steps; j++) {
            if (runningTime > currentTime && i < gesturesLength) {
                current = gestures.getJSONObject(i++);
                currentTime = current.getDouble("time");
            }

            pc[j] = createPointerCoords(current);

            runningTime += 0.005;
        }
//    for(PointerCoords ps : pc){
//    	Logger.info(ps.);
//    }
        return pc;
    }

    private PointerCoords[][] parsePointerCoords(final AndroidCommand command)
            throws JSONException {
        final JSONArray actions = (org.json.JSONArray) command.params().get(
                "actions");

        final double time = computeLongestTime(actions);

        final PointerCoords[][] pcs = new PointerCoords[actions.length()][];
        for (int i = 0; i < actions.length(); i++) {
            final JSONArray gestures = actions.getJSONArray(i);

            pcs[i] = gesturesToPointerCoords(time, gestures);
        }

        return pcs;
    }
}
