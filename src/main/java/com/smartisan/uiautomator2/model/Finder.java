package com.smartisan.uiautomator2.model;

import com.smartisan.uiautomator2.utils.NodeInfoList;

/**
 * Interface for finding UiElement.
 */
public interface Finder {
    /**
     *
     * @param context The starting UiAutomationElement, used as search context
     *
     * @return The matching elements on the current context
     */
    NodeInfoList find(UiElement context);

    /**
     * {@inheritDoc}
     *
     * <p> It is recommended that this method return the description of the finder, for example,
     * "{text=OK}".
     */
    @Override
    String toString();
}
