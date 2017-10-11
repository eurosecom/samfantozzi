package com.eusecom.samfantozzi.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.eusecom.samfantozzi.SamfantozziApp;
import com.eusecom.samfantozzi.rxbus.RxBus;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }


    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    RxBus providesRxBus(Application application) {
        return ((SamfantozziApp) application).getRxBusSingleton();
    }

}
