package com.eusecom.samfantozzi;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class LoggingClickCounterViewModelFactory implements ViewModelProvider.Factory {

    private final LoggingClickInterceptor loggingInterceptor;

    public LoggingClickCounterViewModelFactory(LoggingClickInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoggingClickCounterViewModel.class)) {
            return (T) new LoggingClickCounterViewModel(loggingInterceptor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
