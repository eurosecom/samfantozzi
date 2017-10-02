package com.eusecom.samfantozzi.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class} )
public class ClockModule {

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }


}