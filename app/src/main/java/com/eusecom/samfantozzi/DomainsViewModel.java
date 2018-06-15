package com.eusecom.samfantozzi;

/**
 * ViewModel implementation that logs every count increment.
 */
public class DomainsViewModel extends LogClickCounterViewModel {
    private final LoggingClickInterceptor loggingInterceptor;

    public DomainsViewModel(LoggingClickInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void setCount(int count) {
        super.setCount(count);
        loggingInterceptor.intercept(count);
    }
}
