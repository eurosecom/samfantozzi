package com.eusecom.samfantozzi.retrofit;

import com.eusecom.samfantozzi.models.Attendance;

import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface AbsServerService {

    @GET("/attendance/absserver.php")
    Observable<List<Attendance>> getAbsServer(@Query("fromfir") String fromfir);

    @GET("/attendance/absserver.php")
    Observable<List<Attendance>> setKeyAndgetAbsServer(@Query("fromfir") String fromfir, @Query("keyf") String keyf, @Query("cplxb") String cplxb);

    @GET("/attendance/absserver.json")
    Observable<List<Attendance>> getAbsServerFromJson(@Query("user") String userName);

    @GET("users/{user}/starred")
    Observable<List<Attendance>> getAbsServerFromGitHub(@Path("user") String userName);


}
