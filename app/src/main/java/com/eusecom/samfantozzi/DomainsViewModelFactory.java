package com.eusecom.samfantozzi;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class DomainsViewModelFactory implements ViewModelProvider.Factory {

    private final LoggingClickInterceptor loggingInterceptor;

    public DomainsViewModelFactory(LoggingClickInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DomainsViewModel.class)) {
            return (T) new DomainsViewModel(loggingInterceptor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
