package com.eusecom.samfantozzi;

import android.util.Log;

public class LoggingClickInterceptor {
    private static final String LOG_TAG = LoggingClickInterceptor.class.getSimpleName();

    public void intercept(int clickCount) {
        Log.d(LOG_TAG, "processed click " + clickCount);
    }
}
