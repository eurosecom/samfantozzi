package com.eusecom.samfantozzi.retrofit;

import com.eusecom.samfantozzi.Account;
import com.eusecom.samfantozzi.CompanyKt;
import com.eusecom.samfantozzi.IdCompanyKt;
import com.eusecom.samfantozzi.Invoice;
import com.eusecom.samfantozzi.models.Attendance;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface AbsServerService {

    @GET("/androidfantozzi/control_idcompany.php")
    Observable<List<IdCompanyKt>> controlIdCompanyOnSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh, @Query("queryx") String queryx);

    @GET("/attendance/absserver.php")
    Observable<List<Attendance>> getAbsServer(@Query("fromfir") String fromfir);

    @GET("/androidfantozzi/get_all_firmy.php")
    Observable<List<CompanyKt>> getCompaniesFromServer(@Query("userhash") String userhash, @Query("userid") String userid);

    @GET("/androidfantozzi/get_invoices.php")
    Observable<List<Invoice>> getInvoicesFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh
            , @Query("uce") String uce, @Query("ume") String ume);

    @GET("/androidfantozzi/get_accounttypes.php")
    Observable<List<Account>> getAccountsFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh);

    @GET("/attendance/absserver.php")
    Observable<List<Attendance>> getInvoicesFromServer(@Query("fromfir") String fromfir);


}
