package com.eusecom.samfantozzi;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;

import com.eusecom.samfantozzi.dagger.components.DaggerDemoComponent;
import com.eusecom.samfantozzi.dagger.components.DemoComponent;
import com.eusecom.samfantozzi.dagger.modules.ApplicationModule;
import com.eusecom.samfantozzi.di.DaggerAppComponent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


public class SamfantozziApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;


    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }


    private final DemoComponent dgaeacomponent = createDgAeaComponent();

    protected DemoComponent createDgAeaComponent() {
        return DaggerDemoComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public DemoComponent dgaeacomponent() {
        return dgaeacomponent;
    }

}
