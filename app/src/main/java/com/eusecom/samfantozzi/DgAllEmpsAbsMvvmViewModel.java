package com.eusecom.samfantozzi;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.models.Employee;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.samfantozzi.mvvmschedulers.ISchedulerProvider;
import java.util.List;
import java.util.Random;

import io.reactivex.Flowable;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * View model for the CompaniesMvvmActivity.
 */
public class DgAllEmpsAbsMvvmViewModel {

    //@Inject only by Base constructor injection, then i have got all provided dependencies in module DgFirebaseSubModule
    // injected in class DgAllEmpsAbsListFragment where i inject DgAllEmpsAbsMvvmViewModel
    // If i provide dependency DgAllEmpsAbsMvvmViewModel in DgFirebaseSubModule then i have got in DgAllEmpsAbsMvvmViewMode only dependencies in constructor
    DgAllEmpsAbsIDataModel mDataModel;

    //@Inject only by Base constructor injection
    ISchedulerProvider mSchedulerProvider;

    //@Inject only by Base constructor injection
    SharedPreferences mSharedPreferences;

    MCrypt mMcrypt;
    String encrypted;

    //@Inject only by Base constructor injection
    public DgAllEmpsAbsMvvmViewModel(@NonNull final DgAllEmpsAbsIDataModel dataModel,
                                     @NonNull final ISchedulerProvider schedulerProvider,
                                     @NonNull final SharedPreferences sharedPreferences,
                                     @NonNull final MCrypt mcrypt) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
        mSharedPreferences = sharedPreferences;
        mMcrypt = mcrypt;
    }


    //recyclerview method for DgAeaActivity


    //get realmemployees list from FB
    public Observable<List<Employee>> getObservableFBusersRealmEmployee() {

        String usicox = mSharedPreferences.getString("usico", "");
        String usuid = mSharedPreferences.getString("usuid", "");
        int lenmoje=1;
        String ustype = mSharedPreferences.getString("ustype", "");
        if (ustype.equals("99")) {
            lenmoje=0;
        }else{

        }
        return mDataModel.getObservableFBusersRealmEmployee(usicox, usuid, lenmoje);
    }
    //end get realmemployees list from FB

    //get absences from mock
    public Observable<List<Attendance>> getMyAbsencesFromMock() {

        String firx = mSharedPreferences.getString("fir", "0");

        return mDataModel.getAbsencesFromMock(firx);
    }
    //end get absences from mock

    //get absences from server
    public Observable<List<Attendance>> getMyAbsencesFromServer() {

        String usicox = mSharedPreferences.getString("usico", "");
        String firx = mSharedPreferences.getString("fir", "0");
        if (usicox.equals("44551142999")) {
            firx = "37";
        }


        return mDataModel.getAbsencesFromMysqlServer(firx);
    }
    //end get absences from server

    //recyclerview method for SupplierListActivity

    //get invoices from MySql server
    public Observable<List<Invoice>> getMyInvoicesFromSqlServer(String drh) {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;
        encrypted = "";


        try {
            encrypted = mMcrypt.bytesToHex( mMcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Log.d("userxplus ", encrypted + " " + ds);
        	/* Decrypt */
        //String decrypted = new String( mMcrypt.decrypt( encrypted ) );

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = mSharedPreferences.getString("doduce", "");
        if (drh.equals("1")) {
            dodx = mSharedPreferences.getString("odbuce", "");
        }
        if (drh.equals("3")) {
            dodx = mSharedPreferences.getString("pokluce", "");
        }
        if (drh.equals("4")) {
            dodx = mSharedPreferences.getString("bankuce", "");
        }
        String umex = mSharedPreferences.getString("ume", "");

        return mDataModel.getInvoicesFromMysqlServer(encrypted, ds, firx, rokx, drh, dodx, umex);
    }
    //end get invoices from MySql server

    //get invoices from server
    public Observable<List<Attendance>> getMyInvoicesFromServer() {

        String usicox = mSharedPreferences.getString("usico", "");
        String firx = mSharedPreferences.getString("fir", "0");
        if (usicox.equals("44551142999")) {
            firx = "37";
        }


        return mDataModel.getInvoicesFromServer(firx);
    }
    //end get invoices from server


    //recyclerview method for ChooseMonthActivity

    //get months
    public Observable<List<Month>> getMonth() {

        String rokx = mSharedPreferences.getString("rok", "0");

        return mDataModel.getMonthForYear(rokx);
    }
    //end get months

    //recyclerview method for ChooseAccountActivity

    //get accounts from MySql server
    public Observable<List<Account>> getMyAccountsFromSqlServer(String drh) {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;
        encrypted = "";


        try {
            encrypted = mMcrypt.bytesToHex( mMcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Log.d("userxplus ", encrypted + " " + ds);
        	/* Decrypt */
        //String decrypted = new String( mMcrypt.decrypt( encrypted ) );

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");

        return mDataModel.getAccountsFromMysqlServer(encrypted, ds, firx, rokx, drh);
    }
    //end get accounts from MySql server

    //get accounts
    public Observable<List<Account>> getAccounts() {

        String rokx = mSharedPreferences.getString("rok", "0");

        return mDataModel.getAccounts(rokx);
    }
    //end get accounts


    //recyclerview method for ChooseCompanyActivity

    //get companies from MySql server
    public Observable<List<CompanyKt>> getMyCompaniesFromServer() {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;
        encrypted = "";


        try {
            encrypted = mMcrypt.bytesToHex( mMcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Log.d("userxplus ", encrypted + " " + ds);
        	/* Decrypt */
        //String decrypted = new String( mMcrypt.decrypt( encrypted ) );

        return mDataModel.getCompaniesFromMysqlServer(encrypted, ds);
    }
    //end get companies from MySql server


    //recyclerview method for CashListKtFragment
    //get PDF Uri document
    public void emitDocumentPdfUri(Invoice invx) { mObservableDocPDF.onNext(invx); }

    @NonNull
    private BehaviorSubject<Invoice> mObservableDocPDF = BehaviorSubject.create();

    @NonNull
    public Observable<Uri> getObservableDocPdf() {

        String firx = mSharedPreferences.getString("fir", "");
        //String rokx = "2014";
        String rokx = mSharedPreferences.getString("rok", "");
        //String serverx = "www.eshoptest.sk";
        String serverx = mSharedPreferences.getString("servername", "");
        //String adresx = "www.eshoptest.sk/androiducto";
        String adresx = mSharedPreferences.getString("servername", "") + "/androiducto";

        String usuidx = mSharedPreferences.getString("usuid", "");

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String userx = "Nick/test2345" + "/ID/1001" + "/PSW/cp41cs" + "/Doklad/" + ds;

        String userxplus = userx + "/" + usuidx;

        encrypted = "";

        try {
            encrypted = mMcrypt.bytesToHex( mMcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return mObservableDocPDF
                .observeOn(mSchedulerProvider.ui())
                .flatMap(invx ->
                        mDataModel.getObservableUriDocPdf(invx, firx, rokx, serverx, adresx, encrypted));
    }

    public void clearObservableDocPDF() {

        mObservableDocPDF = BehaviorSubject.create();

    }
    //end get PDF Uri document

    //emit CashList search query
    public void emitMyObservableCashListQuery(String queryx) { mObservableCashListQuery.onNext(queryx); }

    @NonNull
    private BehaviorSubject<String> mObservableCashListQuery = BehaviorSubject.create();

    @NonNull
    public Observable<String> getMyObservableCashListQuery() {

        return mObservableCashListQuery
                .observeOn(mSchedulerProvider.ui())
                .flatMap(queryx -> mDataModel.getObservableCashListQuery(queryx));
    }

    public void clearObservableCashListQuery() {

        mObservableCashListQuery = BehaviorSubject.create();

    }
    //end emit CashList search query

    //get absences from FB for update realm
    public void emitAbsencesFromFBforRealm(String dokx) { mObservableAbsencesFromFB.onNext(dokx); }

    @NonNull
    private BehaviorSubject<String> mObservableAbsencesFromFB = BehaviorSubject.create();

    @NonNull
    public Observable<List<Attendance>> getObservableFromFBforRealm() {
        String usicox = "44551142";
        String usuid = "K6u6ay4ghKbXRh7ZJTAEBoKLazm2";
        String ustype = "99";
        String umex = "07.2017";

        //String usicox = "44551142";
        return mObservableAbsencesFromFB
                .observeOn(mSchedulerProvider.ui())
                .flatMap(dokx -> mDataModel.getObservableAbsencesFromFB(dokx, umex, usicox, usuid, ustype));
    }

    public void clearObservableAbsencesFromFB() {

        mObservableAbsencesFromFB = BehaviorSubject.create();

    }
    //end get absences from FB for update realm



    //NewCashDocFragment and NewCashDocActivity

    //emit Observable<Account> control IdCompany
    public void emitMyObservableIdModelCompany(String queryx) {
        //String querys = String.valueOf(queryx);
        mObservableIdModelCompany.onNext(queryx);
    }

    @NonNull
    private BehaviorSubject<String> mObservableIdModelCompany = BehaviorSubject.create();

    @NonNull
    public Observable<List<Account>> getMyObservableIdModelCompany() {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;
        encrypted = "";


        try {
            encrypted = mMcrypt.bytesToHex( mMcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Log.d("userxplus ", encrypted + " " + ds);
        	/* Decrypt */
        //String decrypted = new String( mMcrypt.decrypt( encrypted ) );

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "2";

        return mObservableIdModelCompany
                .observeOn(mSchedulerProvider.ui())
                .flatMap(queryx -> mDataModel.getObservableIdModelCompany(encrypted, ds, firx, rokx, drh, queryx ));
    }

    public void clearObservableIdModelCompany() {

        mObservableIdModelCompany = BehaviorSubject.create();

    }
    //end emit Observable<Account> control IdCompany



    //emit Observable<Boolean> control IdCompany
    public void emitMyObservableIdCompany(String queryx) {
        //String querys = String.valueOf(queryx);
        mObservableIdCompany.onNext(queryx);
    }

    @NonNull
    private BehaviorSubject<String> mObservableIdCompany = BehaviorSubject.create();

    @NonNull
    public Observable<Boolean> getMyObservableIdCompany() {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;
        encrypted = "";


        try {
            encrypted = mMcrypt.bytesToHex( mMcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Log.d("userxplus ", encrypted + " " + ds);
        	/* Decrypt */
        //String decrypted = new String( mMcrypt.decrypt( encrypted ) );

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "2";

        return mObservableIdCompany
                .observeOn(mSchedulerProvider.ui())
                .flatMap(queryx -> mDataModel.getObservableIdCompany(encrypted, ds, firx, rokx, drh, queryx ));
    }

    public void clearObservableIdCompany() {

        mObservableIdCompany = BehaviorSubject.create();

    }
    //end emit Observable<Boolean> control IdCompany



}
