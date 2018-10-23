package com.testguard.uiautomator2.handler;


import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.accessibility.AccessibilityNodeInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import com.testguard.uiautomator2.common.exceptions.ElementNotFoundException;
import com.testguard.uiautomator2.common.exceptions.InvalidSelectorException;
import com.testguard.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.testguard.uiautomator2.common.exceptions.UiSelectorSyntaxException;
import com.testguard.uiautomator2.core.AccessibilityNodeInfoGetter;
import com.testguard.uiautomator2.executorserver.AndroidCommand;
import com.testguard.uiautomator2.executorserver.AndroidCommandResult;
import com.testguard.uiautomator2.model.AndroidElement;
import com.testguard.uiautomator2.model.By;
import com.testguard.uiautomator2.model.By.ById;
import com.testguard.uiautomator2.model.By.ByName;
import com.testguard.uiautomator2.model.KnownElements;
import com.testguard.uiautomator2.model.XPathFinder;
import com.testguard.uiautomator2.model.internal.CustomUiDevice;
import com.testguard.uiautomator2.model.internal.NativeAndroidBySelector;
import com.testguard.uiautomator2.executorserver.WDStatus;
import com.testguard.uiautomator2.utils.Device;
import com.testguard.uiautomator2.utils.ElementHelpers;
import com.testguard.uiautomator2.utils.Logger;
import com.testguard.uiautomator2.utils.NodeInfoList;
import com.testguard.uiautomator2.utils.UiAutomatorParser;
import static com.testguard.uiautomator2.handler.FindElement.getElementLocator;

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
 */
public class FindElements extends BaseCommandHandler {


    private static final Pattern endsWithInstancePattern = Pattern.compile(".*INSTANCE=\\d+]$");

    /*
     * @param command The {@link AndroidCommand} used for this handler.
     *
     * @return {@link AndroidCommandResult}
     *
     * @throws JSONException
     *
     * @see com.yep.android.bootstrap.BaseCommandHandler#execute(com.yep.android.
     * bootstrap.AndroidCommand)
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
        final Hashtable<String, Object> params = command.params();
        JSONArray result = new JSONArray();
        try{
            Logger.info("Find elements command");
            KnownElements ke = KnownElements.getInstance();
            final String method = (String) params.get("strategy");
            final String selector = (String) params.get("selector");
            final String contextId = (String) params.get("context");
            Logger.info(String.format("find element command using '%s' with selector '%s'.", method, selector));
            final By by = new NativeAndroidBySelector().pickFrom(method, selector);
            Device.getInstance().getUiDevice().waitForIdle();
            List<Object> elements ;
            if(contextId.length() > 0) {
                elements = this.findElements(by, contextId);
            } else {
                elements = this.findElements(by);
            }
            for (Object element : elements) {
                String id = UUID.randomUUID().toString();
                AndroidElement androidElement = Device.getInstance().getAndroidElement(id, element, by);
                ke.add(androidElement);
                JSONObject jsonElement = new JSONObject();
                jsonElement.put("ELEMENT", id);
                result.put(jsonElement);
            }
            return getSuccessResult(result);
        }catch (ElementNotFoundException ignored) {
            /* For findElements up on no Element. instead of throwing exception unlike in findElement,
               empty array should be return. for more info refer:
               https://github.com/SeleniumHQ/selenium/wiki/JsonWireProtocol#sessionsessionidelements
              */
            return getSuccessResult(result);
        } catch (UnsupportedOperationException e) {
            return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR, e);
        } catch (InvalidSelectorException e) {
            return new AndroidCommandResult( WDStatus.INVALID_SELECTOR, e);
        } catch (ParserConfigurationException e) {
            return new AndroidCommandResult( WDStatus.UNKNOWN_ERROR, e);
        } catch (ClassNotFoundException e) {
            return new AndroidCommandResult( WDStatus.UNKNOWN_ERROR, e);
        } catch (JSONException e) {
            return new AndroidCommandResult( WDStatus.JSON_DECODER_ERROR, e);
        } catch (UiSelectorSyntaxException e) {
            return new AndroidCommandResult( WDStatus.UNKNOWN_COMMAND, e);
        } catch (UiAutomator2Exception e) {
            return new AndroidCommandResult( WDStatus.UNKNOWN_ERROR, e);
        } catch (UiObjectNotFoundException e) {
            return new AndroidCommandResult( WDStatus.NO_SUCH_ELEMENT);
        }

    }

    private List<Object> findElements(By by) throws ElementNotFoundException, ParserConfigurationException, ClassNotFoundException, InvalidSelectorException, UiAutomator2Exception, UiSelectorSyntaxException {
        if (by instanceof By.ById) {
            String locator = getElementLocator((ById) by);
            return CustomUiDevice.getInstance().findObjects(android.support.test.uiautomator.By.res(locator));
        } else if (by instanceof By.ByName) {
            return CustomUiDevice.getInstance().findObjects(android.support.test.uiautomator.By.text(by.getElementLocator()));
        } else if (by instanceof By.ByAccessibilityId) {
            return CustomUiDevice.getInstance().findObjects(android.support.test.uiautomator.By.desc(by.getElementLocator()));
        } else if (by instanceof By.ByClass) {
            return CustomUiDevice.getInstance().findObjects(android.support.test.uiautomator.By.clazz(by.getElementLocator()));
        } else if (by instanceof By.ByXPath) {
            //TODO: need to handle the context parameter in a smart way
            return getXPathUiObjects(by.getElementLocator(), null /* AndroidElement */);
        } else if (by instanceof By.ByAndroidUiAutomator) {
            //TODO: need to handle the context parameter in a smart way
            return getUiObjectsUsingAutomator(findByUiAutomator(by.getElementLocator()), "");
        }

        String msg = String.format("By locator %s is curently not supported!", by.getClass().getSimpleName());
        throw new UnsupportedOperationException(msg);
    }

    private List<Object> findElements(By by, String contextId) throws InvalidSelectorException, ParserConfigurationException, ClassNotFoundException, UiSelectorSyntaxException, UiAutomator2Exception, UiObjectNotFoundException {

        AndroidElement element = KnownElements.getInstance().getElementFromCache(contextId);
        if (element == null) {
            throw new ElementNotFoundException();
        }
        if (by instanceof ById) {
            String locator = getElementLocator((ById) by);
            return element.getChildren(android.support.test.uiautomator.By.res(locator), by);
        }else if(by instanceof ByName){
            return element.getChildren(android.support.test.uiautomator.By.text(by.getElementLocator()),by);
        } else if (by instanceof By.ByAccessibilityId) {
            return element.getChildren(android.support.test.uiautomator.By.desc(by.getElementLocator()), by);
        } else if (by instanceof By.ByClass) {
            return element.getChildren(android.support.test.uiautomator.By.clazz(by.getElementLocator()), by);
        } else if (by instanceof By.ByXPath) {
            return getXPathUiObjects(by.getElementLocator(), element);
        } else if (by instanceof By.ByAndroidUiAutomator) {
            return getUiObjectsUsingAutomator(findByUiAutomator(by.getElementLocator()), contextId);
        }
        String msg = String.format("By locator %s is currently not supported!", by.getClass().getSimpleName());
        throw new UnsupportedOperationException(msg);
    }

    /**
     * returns  UiObject2 for an xpath expression
     **/
    private static List<Object> getXPathUiObjects(final String expression, AndroidElement element) throws ElementNotFoundException, ParserConfigurationException, InvalidSelectorException, ClassNotFoundException, UiAutomator2Exception {
        AccessibilityNodeInfo nodeInfo = null;
        if (element != null) {
            nodeInfo = AccessibilityNodeInfoGetter.fromUiObject(element.getUiObject());
        }
        final NodeInfoList nodeList = XPathFinder.getNodesList(expression, nodeInfo);
        if (nodeList.size() == 0) {
            throw new ElementNotFoundException();
        }
        return CustomUiDevice.getInstance().findObjects(nodeList);
    }

    public List<UiSelector> findByUiAutomator(String expression) throws UiSelectorSyntaxException {
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
        return selectors;
    }

    /**
     * returns  List<UiObject> using '-android automator' expression
     **/
    private List<Object> getUiObjectsUsingAutomator(List<UiSelector> selectors, String contextId) throws InvalidSelectorException, ClassNotFoundException {
        List<Object> foundElements = new ArrayList<Object>();
        for (final UiSelector sel : selectors) {
            // With multiple selectors, we expect that some elements may not
            // exist.
            try {
                Logger.debug("Using: " + sel.toString());
                final List<Object> elementsFromSelector = fetchElements(sel, contextId);
                foundElements.addAll(elementsFromSelector);
            } catch (final UiObjectNotFoundException ignored) {
                //for findElements up on no elements, empty array should return.
            }
        }
        foundElements = ElementHelpers.dedupe(foundElements);
        return foundElements;
    }

    /**
     * finds elements with given UiSelector return List<UiObject
     *
     * @param sel
     * @param key
     * @return
     */
    private List<Object> fetchElements(UiSelector sel, String key) throws UiObjectNotFoundException, ClassNotFoundException, InvalidSelectorException {
        //TODO: finding elements with contextId yet to implement
        boolean keepSearching = true;
        final String selectorString = sel.toString();
        final boolean useIndex = selectorString.contains("CLASS_REGEX=");
        final boolean endsWithInstance = endsWithInstancePattern.matcher(selectorString).matches();
        Logger.debug("getElements selector:" + selectorString);
        final ArrayList<Object> elements = new ArrayList<Object>();

        // If sel is UiSelector[CLASS=android.widget.Button, INSTANCE=0]
        // then invoking instance with a non-0 argument will corrupt the selector.
        //
        // sel.instance(1) will transform the selector into:
        // UiSelector[CLASS=android.widget.Button, INSTANCE=1]
        //
        // The selector now points to an entirely different element.
        if (endsWithInstance) {
            Logger.debug("Selector ends with instance.");
            // There's exactly one element when using instance.
            UiObject instanceObj = Device.getInstance().getUiDevice().findObject(sel);
            if (instanceObj != null && instanceObj.exists()) {
                elements.add(instanceObj);
            }
            return elements;
        }

        UiObject lastFoundObj;
        final AndroidElement baseEl = KnownElements.getInstance().getElementFromCache(key);

        UiSelector tmp;
        int counter = 0;
        while (keepSearching) {
            if (baseEl == null) {
                Logger.debug("Element[" + key + "] is null: (" + counter + ")");

                if (useIndex) {
                    Logger.debug("  using index...");
                    tmp = sel.index(counter);
                } else {
                    tmp = sel.instance(counter);
                }

                Logger.debug("getElements tmp selector:" + tmp.toString());
                lastFoundObj = Device.getInstance().getUiDevice().findObject(tmp);
            } else {
                Logger.debug("Element[" + key + "] is " + baseEl.getId() + ", counter: "
                        + counter);
                lastFoundObj = (UiObject) baseEl.getChild(sel.instance(counter));
            }
            counter++;
            if (lastFoundObj != null && lastFoundObj.exists()) {
                elements.add(lastFoundObj);
            } else {
                keepSearching = false;
            }
        }
        return elements;

    }


}
