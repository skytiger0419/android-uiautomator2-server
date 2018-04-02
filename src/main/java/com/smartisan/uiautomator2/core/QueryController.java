package com.smartisan.uiautomator2.core;

import android.view.accessibility.AccessibilityNodeInfo;
import com.smartisan.uiautomator2.common.exceptions.UiAutomator2Exception;
import static com.smartisan.uiautomator2.utils.ReflectionUtils.invoke;
import static com.smartisan.uiautomator2.utils.ReflectionUtils.method;

public class QueryController {

    private static final String CLASS_QUERY_CONTROLLER = "android.support.test.uiautomator.QueryController";
    private static final String METHOD_GET_ACCESSIBILITY_ROOT_NODE = "getRootNode";

    private final Object queryController;

    public QueryController(Object queryController) {
        this.queryController = queryController;
    }

    public AccessibilityNodeInfo getAccessibilityRootNode() throws UiAutomator2Exception {
        return (AccessibilityNodeInfo) invoke(method(CLASS_QUERY_CONTROLLER, METHOD_GET_ACCESSIBILITY_ROOT_NODE), queryController);
    }

}
