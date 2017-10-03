package com.eusecom.samfantozzi.retrofit;

import android.support.annotation.NonNull;

import com.eusecom.samfantozzi.models.Attendance;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by chris on 6/1/16.
 */
public class AbsServerClient {


    private static AbsServerClient instance;
    private AbsServerService absServerService;

    private AbsServerClient(String urlx) {

        final String ABSSERVER_BASE_URL = urlx;
        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(ABSSERVER_BASE_URL)
                                                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                                        .build();
        absServerService = retrofit.create(AbsServerService.class);
    }

    public static AbsServerClient getInstance(String urlx) {
        if (instance == null) {
            instance = new AbsServerClient(urlx);
        }
        return instance;
    }

    public Observable<List<Attendance>> getAbsServer(@NonNull String fromfir) {
        return absServerService.getAbsServer(fromfir);
    }

    public Observable<List<Attendance>> setKeyAndgetAbsServer(@NonNull String fromfir, @NonNull String keyf, @NonNull String cplxb) {
        return absServerService.setKeyAndgetAbsServer(fromfir, keyf, cplxb);
    }
}
