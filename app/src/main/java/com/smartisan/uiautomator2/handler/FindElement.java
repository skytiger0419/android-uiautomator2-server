package com.smartisan.uiautomator2.handler;


import android.support.test.uiautomator.Configurator;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.accessibility.AccessibilityNodeInfo;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import com.smartisan.uiautomator2.common.exceptions.ElementNotFoundException;
import com.smartisan.uiautomator2.common.exceptions.InvalidSelectorException;
import com.smartisan.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.smartisan.uiautomator2.common.exceptions.UiSelectorSyntaxException;
import com.smartisan.uiautomator2.core.AccessibilityNodeInfoGetter;
import com.smartisan.uiautomator2.executorserver.AndroidCommand;
import com.smartisan.uiautomator2.executorserver.AndroidCommandResult;
import com.smartisan.uiautomator2.model.AndroidElement;
import com.smartisan.uiautomator2.model.By;
import com.smartisan.uiautomator2.model.By.ByClass;
import com.smartisan.uiautomator2.model.By.ById;
import com.smartisan.uiautomator2.model.By.ByName;
import com.smartisan.uiautomator2.model.KnownElements;
import com.smartisan.uiautomator2.model.Session;
import com.smartisan.uiautomator2.model.XPathFinder;
import com.smartisan.uiautomator2.model.internal.CustomUiDevice;
import com.smartisan.uiautomator2.model.internal.NativeAndroidBySelector;
import com.smartisan.uiautomator2.executorserver.WDStatus;
import com.smartisan.uiautomator2.utils.Device;
import com.smartisan.uiautomator2.utils.Logger;
import com.smartisan.uiautomator2.utils.NodeInfoList;
import com.smartisan.uiautomator2.utils.UiAutomatorParser;

/**
 * java_package : type / name
 * <p>
 * com.example.Test:id/enter
 * <p>
 * ^[a-zA-Z_] - Java package must start with letter or underscore
 * [a-zA-Z0-9\._]* - Java package may contain letters, numbers, periods and
 * underscores : - : ends the package and starts the type [^\/]+ - type is
 * made up of at least one non-/ characters \\/ - / ends the type and starts
 * the name [\S]+$ - the name contains at least one non-space character and
 * then the line is ended
 * <p>
 * Example:
 * http://java-regex-tester.appspot.com/regex/5f04ac92-f9aa-45a6-b1dc-e2c25fd3cc6b
 */
public class FindElement extends BaseCommandHandler {

    static final Pattern resourceIdRegex = Pattern
            .compile("^[a-zA-Z_][a-zA-Z0-9\\._]*:[^\\/]+\\/[\\S]+$");

    /**
     * @param command The {@link AndroidCommand} used for this handler.
     * @return {@link AndroidCommandResult}
     * @throws JSONException
     */
    @Override
    public AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException {
        return execute(command, false);
    }

    /**
     * execute implementation.
     *
     * @param command The {@link AndroidCommand} used for this handler.
     * @param isRetry Is this invocation a second attempt?
     * @return {@link AndroidCommandResult}
     * @throws JSONException
     */
    private AndroidCommandResult execute(final AndroidCommand command,
                                         final boolean isRetry) throws JSONException {
        Configurator.getInstance().setWaitForIdleTimeout(5);
        final Hashtable<String, Object> params = command.params();
        try {
            Logger.info("Find element command");
            KnownElements ke = KnownElements.getInstance();
            final String method = (String) params.get("strategy");
            final String selector = (String) params.get("selector");
            final String contextId = (String) params.get("context");
            Object element;
            Logger.info(String.format("find element command using '%s' with selector '%s'.", method, selector));
            final By by = new NativeAndroidBySelector().pickFrom(method, selector);
            Device.getInstance().getUiDevice().waitForIdle();
            if (contextId.length() > 0) {
                element = this.findElement(by, contextId);
            } else {
                element = this.findElement(by);
            }
            if (element == null) {
                return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
            } else {
                String id = UUID.randomUUID().toString();
                AndroidElement androidElement = Device.getInstance().getAndroidElement(id, element, by);
                ke.add(androidElement);
                JSONObject result = new JSONObject();
                result.put("ELEMENT", id);
                return getSuccessResult(result);
            }
        } catch (UnsupportedOperationException e) {
            return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR, e);
        } catch (InvalidSelectorException e) {
            return new AndroidCommandResult(WDStatus.INVALID_SELECTOR, e);
        } catch (ElementNotFoundException e) {
            return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
        } catch (ParserConfigurationException e) {
            return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR, e);
        } catch (ClassNotFoundException e) {
            return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR, e);
        } catch (JSONException e) {
            return new AndroidCommandResult(WDStatus.JSON_DECODER_ERROR, e);
        } catch (UiSelectorSyntaxException e) {
            return new AndroidCommandResult(WDStatus.UNKNOWN_COMMAND, e);
        } catch (UiAutomator2Exception e) {
            return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR, e);
        } catch (UiObjectNotFoundException e) {
            return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
        }

    }

    private Object findElement(By by) throws InvalidSelectorException, ElementNotFoundException, ParserConfigurationException, ClassNotFoundException, UiSelectorSyntaxException, UiAutomator2Exception {
        if (by instanceof ById) {
            String locator = getElementLocator((ById) by);
            return CustomUiDevice.getInstance().findObject(android.support.test.uiautomator.By.res(locator));
        }else if(by instanceof ByName){
            return CustomUiDevice.getInstance().findObject(android.support.test.uiautomator.By.text(by.getElementLocator()));
        } else if (by instanceof By.ByAccessibilityId) {
            return CustomUiDevice.getInstance().findObject(android.support.test.uiautomator.By.desc(by.getElementLocator()));
        } else if (by instanceof ByClass) {
            return CustomUiDevice.getInstance().findObject(android.support.test.uiautomator.By.clazz(by.getElementLocator()));
        } else if (by instanceof By.ByXPath) {
            return getXPathUiObject(by.getElementLocator(), null /* AndroidElement */);
        } else if (by instanceof By.ByAndroidUiAutomator) {
            return CustomUiDevice.getInstance().findObject(findByUiAutomator(by.getElementLocator()));
        }
        String msg = String.format("By locator %s is currently not supported!", by.getClass().getSimpleName());
        throw new UnsupportedOperationException(msg);
    }

    private Object findElement(By by, String contextId) throws InvalidSelectorException,
            ParserConfigurationException, ClassNotFoundException,
            UiSelectorSyntaxException, UiAutomator2Exception, UiObjectNotFoundException {

        AndroidElement element = KnownElements.getInstance().getElementFromCache(contextId);
        if (element == null) {
            throw new ElementNotFoundException();
        }
        if (by instanceof ById) {
            String locator = getElementLocator((ById) by);
            return element.getChild(android.support.test.uiautomator.By.res(locator));
        }else if(by instanceof ByName){
            return element.getChild(android.support.test.uiautomator.By.text(by.getElementLocator()));
        } else if (by instanceof By.ByAccessibilityId) {
            return element.getChild(android.support.test.uiautomator.By.desc(by.getElementLocator()));
        } else if (by instanceof ByClass) {
            return element.getChild(android.support.test.uiautomator.By.clazz(by.getElementLocator()));
        } else if (by instanceof By.ByXPath) {
            return getXPathUiObject(by.getElementLocator(), element);
        } else if (by instanceof By.ByAndroidUiAutomator) {
            return element.getChild(findByUiAutomator(by.getElementLocator()));
        }
        String msg = String.format("By locator %s is currently not supported!", by.getClass().getSimpleName());
        throw new UnsupportedOperationException(msg);

    }

    /**
     * returns  UiObject2 for an xpath expression
     * TODO: Need to handle contextId based finding
     */
    private static Object getXPathUiObject(final String expression, AndroidElement element) throws ParserConfigurationException, InvalidSelectorException, ClassNotFoundException, UiAutomator2Exception {
        AccessibilityNodeInfo nodeInfo = null;
        if (element != null) {
            nodeInfo = AccessibilityNodeInfoGetter.fromUiObject(element.getUiObject());
        }
        final NodeInfoList nodeList = XPathFinder.getNodesList(expression, nodeInfo /* AccessibilityNodeInfo */);
        if (nodeList.size() == 0) {
            throw new ElementNotFoundException();
        }
        return CustomUiDevice.getInstance().findObject(nodeList);
    }

    /**
     * finds the UiSelector for given expression
     */
    public UiSelector findByUiAutomator(String expression) throws UiSelectorSyntaxException {
        List<UiSelector> parsedSelectors = null;
        UiAutomatorParser uiAutomatorParser = new UiAutomatorParser();
        final List<UiSelector> selectors = new ArrayList<UiSelector>();
        try {
            parsedSelectors = uiAutomatorParser.parse(expression);
        } catch (final UiSelectorSyntaxException e) {
            throw new UiSelectorSyntaxException(
                    "Could not parse UiSelector argument: " + e.getMessage());
        }
        for (final UiSelector selector : parsedSelectors) {
            selectors.add(selector);
        }
        return selectors.get(0);
    }

    public static String getElementLocator(ById by) {
        String locator = by.getElementLocator();
        if (!resourceIdRegex.matcher(by.getElementLocator()).matches()) {
            // not a fully qualified resource id
            // transform "textToBeChanged" into:
            // com.example.android.testing.espresso.BasicSample:id/textToBeChanged
            // it's prefixed with the app package.
            locator = (String) Session.capabilities.get("appPackage") + ":id/" + by.getElementLocator();
            Logger.debug("Updated findElement locator strategy: " + locator);
        }
        return locator;
    }


}
