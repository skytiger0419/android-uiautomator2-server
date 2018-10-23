package com.testguard.uiautomator2.core;

import android.app.UiAutomation;
import android.support.test.uiautomator.UiDevice;
import android.view.Display;
import android.view.InputEvent;

import com.testguard.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.testguard.uiautomator2.utils.Device;
import com.testguard.uiautomator2.utils.Logger;

import static com.testguard.uiautomator2.utils.ReflectionUtils.getField;
import static com.testguard.uiautomator2.utils.ReflectionUtils.invoke;
import static com.testguard.uiautomator2.utils.ReflectionUtils.method;

public class UiAutomatorBridge {

    private static final String CLASS_UI_AUTOMATOR_BRIDGE = "android.support.test.uiautomator.UiAutomatorBridge";

    private static final String FIELD_UI_AUTOMATOR_BRIDGE = "mUiAutomationBridge";
    private static final String FIELD_QUERY_CONTROLLER = "mQueryController";
    private static final String FIELD_INTERACTION_CONTROLLER = "mInteractionController";
    private static final String FIELD_UI_AUTOMATOR = "mUiAutomation";

    private static final String METHOD_GET_DEFAULT_DISPLAY = "getDefaultDisplay";
    private static final String METHOD_INJECT_INPUT_EVENT = "injectInputEvent";

    private static UiAutomatorBridge INSTANCE = new UiAutomatorBridge();

    private final Object uiAutomatorBridge;

    public UiAutomatorBridge() {
        try {

            this.uiAutomatorBridge = getField(UiDevice.class, FIELD_UI_AUTOMATOR_BRIDGE, Device.getInstance().getUiDevice());
        } catch (Error error) {
            Logger.error("ERROR", error);
            throw error;
        } catch (UiAutomator2Exception error) {
            Logger.error("ERROR", error);
            throw new Error(error);
        }
    }

    public static UiAutomatorBridge getInstance() {
        return INSTANCE;
    }

    public InteractionController getInteractionController() throws UiAutomator2Exception {
        return new InteractionController(getField(CLASS_UI_AUTOMATOR_BRIDGE, FIELD_INTERACTION_CONTROLLER, uiAutomatorBridge));
    }

    public QueryController getQueryController() throws UiAutomator2Exception {
        return new QueryController(getField(CLASS_UI_AUTOMATOR_BRIDGE, FIELD_QUERY_CONTROLLER, uiAutomatorBridge));
    }

    public UiAutomation getUiAutomation() {
        return (UiAutomation)getField(CLASS_UI_AUTOMATOR_BRIDGE, FIELD_UI_AUTOMATOR, uiAutomatorBridge);
    }

    public Display getDefaultDisplay() throws UiAutomator2Exception {
        return (Display) invoke(method(CLASS_UI_AUTOMATOR_BRIDGE, METHOD_GET_DEFAULT_DISPLAY), uiAutomatorBridge);
    }

    public boolean injectInputEvent(InputEvent event, boolean sync) throws UiAutomator2Exception {
        return (Boolean) invoke(method(CLASS_UI_AUTOMATOR_BRIDGE, METHOD_INJECT_INPUT_EVENT, InputEvent.class, boolean.class), uiAutomatorBridge, event, sync);
    }
}
