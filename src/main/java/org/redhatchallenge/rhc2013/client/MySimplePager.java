package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.Range;

/**
 * Created with IntelliJ IDEA.
 * User: Jun
 * Date: 26/8/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class MySimplePager extends SimplePager{

    public MySimplePager(){
        super();
        this.setRangeLimited(true);
    }

//    public MySimplePager(TextLocation location, Resources resources, boolean showFastForwardButton, int fastForwardRows, boolean showLastPageButton){
//        super(location, resources, showFastForwardButton, fastForwardRows, showLastPageButton);
//        this.setRangeLimited(true);
//    }


    public MySimplePager(TextLocation location, boolean showFastForwardButton, int fastForwardRows, boolean showLastPageButton) {
        super(location, showFastForwardButton, fastForwardRows, showLastPageButton);    //To change body of overridden methods use File | Settings | File Templates.
        this.setRangeLimited(true);
    }

    public void setPageStart(int index) {
        if (this.getDisplay() != null) {
            Range range = getDisplay().getVisibleRange();
            int pageSize = range.getLength();
            if (!isRangeLimited() && getDisplay().isRowCountExact()) {
                index = Math.min(index, getDisplay().getRowCount() - pageSize);
            }
            index = Math.max(0, index);
            if (index != range.getStart()) {
                getDisplay().setVisibleRange(index, pageSize);
            }
        }
    }
}
