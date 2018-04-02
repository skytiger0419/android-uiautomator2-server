package com.smartisan.uiautomator2.model;


import java.util.List;
import java.util.NoSuchElementException;

public interface SearchContext {
    /**
     * Find all elements within the current context using the given mechanism.
     *
     * @param by The locating mechanism to use
     * @return A list of all {@link AndroidElement}s, or an empty list if nothing matches
     */
    List<AndroidElement> findElements(By by);


    /**
     * Find the first {@link AndroidElement} using the given method.
     *
     * @param by The locating mechanism
     * @return The first matching element on the current context
     * @throws NoSuchElementException If no matching elements are found
     */
    AndroidElement findElement(By by);
}
