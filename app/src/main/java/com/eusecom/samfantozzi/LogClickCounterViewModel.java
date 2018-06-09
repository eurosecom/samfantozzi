package com.eusecom.samfantozzi;

import android.arch.lifecycle.ViewModel;

/**
 * Basic view model implementation to illustrate the ViewModel functionality.
 */

public class LogClickCounterViewModel extends ViewModel {
    private int count;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
