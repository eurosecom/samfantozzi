package com.eusecom.samfantozzi.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.eusecom.samfantozzi.DgAllEmpsAbsMvvmViewModel;
import com.eusecom.samfantozzi.SamfantozziApp;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsDataModel;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.samfantozzi.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.google.firebase.database.DatabaseReference;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import okhttp3.Cache;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module(includes = {ApplicationModule.class} )
public class ClockModule {

    String mBaseUrl = "http:\\www.edcom.sk";

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides @Named("cached")
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        return okHttpClient;
    }

    @Provides @Named("non_cached")
    @Singleton
    OkHttpClient provideOkHttpClientNonCached() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        return okHttpClient;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, @Named("cached") OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public AbsServerService providesAbsServerService(Retrofit retrofit) {
        return retrofit.create(AbsServerService.class);
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