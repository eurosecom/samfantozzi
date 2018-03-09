package com.eusecom.samfantozzi.retrofit;

import com.eusecom.samfantozzi.Account;
import com.eusecom.samfantozzi.CompanyKt;
import com.eusecom.samfantozzi.IdCompanyKt;
import com.eusecom.samfantozzi.Invoice;
import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.realm.RealmInvoice;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface AbsServerService {

    @POST("/androidfantozzi/delete_invoice.php")
    @FormUrlEncoded
    Observable<List<Invoice>> deleteInvoiceFromMysqlPost(@Field("userhash") String userhash
            , @Field("userid") String userid, @Field("fromfir") String fromfir
            , @Field("vyb_rok") String vyb_rok, @Field("drh") String drh
            , @Field("invx") String invx);

    @POST("/androidfantozzi/save_invoice.php")
    @FormUrlEncoded
    Observable<List<Invoice>> saveInvoiceToMysqlPost(@Field("userhash") String userhash
            , @Field("userid") String userid, @Field("fromfir") String fromfir
            , @Field("vyb_rok") String vyb_rok, @Field("drh") String drh
            , @Field("invx") String invx, @Field("edidok") String edidok);

    @GET("/androidfantozzi/get_drhpohyby.php")
    Observable<List<Account>> getReceiptExpensesFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh
            , @Query("drupoh") String drhupoh, @Query("ucto") String ucto);

    @GET("/androidfantozzi/get_idcompany.php")
    Observable<List<IdCompanyKt>> getAllIdCompanyOnSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh, @Query("queryx") String queryx);

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
            , @Query("uce") String uce, @Query("ume") String ume, @Query("dokx") String dokx);

    @GET("/androidfantozzi/get_accounttypes.php")
    Observable<List<Account>> getAccountsFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh);

    @GET("/attendance/absserver.php")
    Observable<List<Attendance>> getInvoicesFromServer(@Query("fromfir") String fromfir);

    @GET("/androidfantozzi/example.json")
    Observable<List<Invoice>> getExample(@Query("fromfir") String fromfir);

    @GET("/androidfantozzi/bankitems.json")
    Observable<List<BankItem>> getBankItemsJSON(@Query("fromfir") String fromfir);

    @GET("/androidfantozzi/get_accountitem.php")
    Observable<List<BankItem>> getBankItemsFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh
            , @Query("uce") String uce, @Query("ume") String ume, @Query("dokx") String dokx);


}
