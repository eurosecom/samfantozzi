package com.eusecom.samfantozzi.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.eusecom.samfantozzi.DgAllEmpsAbsMvvmViewModel;
import com.eusecom.samfantozzi.SamfantozziApp;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsDataModel;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.samfantozzi.mvvmschedulers.ISchedulerProvider;
import com.google.firebase.database.DatabaseReference;

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

    @Provides
    @Singleton
    public DatabaseReference providesDatabaseReference(Application application) {

        return ((SamfantozziApp) application).getDatabaseFirebaseReference();
    }

    @Provides
    @Singleton
    public DgAllEmpsAbsIDataModel providesDgAllEmpsAbsIDataModel(DatabaseReference databasereference) {
        return new DgAllEmpsAbsDataModel(databasereference);
    }



    @Provides
    @Singleton
    public ISchedulerProvider providesISchedulerProvider(Application application) {

        return ((SamfantozziApp) application).getSchedulerProvider();
    }

    @Provides
    @Singleton
    public DgAllEmpsAbsMvvmViewModel providesDgAllEmpsAbsMvvmViewModel(DgAllEmpsAbsIDataModel dataModel,
                                                                       ISchedulerProvider schedulerProvider,
                                                                       SharedPreferences sharedPreferences) {
        return new DgAllEmpsAbsMvvmViewModel(dataModel, schedulerProvider, sharedPreferences);
    }



}