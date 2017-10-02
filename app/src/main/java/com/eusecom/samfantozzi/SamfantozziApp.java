package com.eusecom.samfantozzi;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import com.eusecom.samfantozzi.dagger.components.DaggerDemoComponent;
import com.eusecom.samfantozzi.dagger.components.DemoComponent;
import com.eusecom.samfantozzi.dagger.modules.ApplicationModule;
import com.eusecom.samfantozzi.di.DaggerAppComponent;
import com.eusecom.samfantozzi.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samfantozzi.mvvmschedulers.SchedulerProvider;
import com.eusecom.samfantozzi.rxbus.RxBus;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;
import javax.inject.Inject;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


public class SamfantozziApp extends MultiDexApplication implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    public RxBus _rxBus;

    @NonNull
    private DatabaseReference mDatabaseReference;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

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




    @NonNull
    public ISchedulerProvider getSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

    @NonNull
    public DatabaseReference getDatabaseFirebaseReference() {
        return mDatabaseReference;
    }


    public RxBus getRxBusSingleton() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }
        return _rxBus;
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
