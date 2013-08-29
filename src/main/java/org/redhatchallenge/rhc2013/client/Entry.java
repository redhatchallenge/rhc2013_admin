package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class Entry implements EntryPoint {

    @Override
    public void onModuleLoad() {
        ContentContainer.INSTANCE.setContent(new TimeslotScreen());
    }
}
