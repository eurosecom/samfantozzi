package com.eusecom.samfantozzi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.models.Employee;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.samfantozzi.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samfantozzi.realm.RealmInvoice;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

import static android.content.ContentValues.TAG;


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
    String encrypted, encrypted2;

    @NonNull
    private CompositeSubscription mSubscription;
    @NonNull
    private ConnectivityManager mConnectivityManager;

    //@Inject only by Base constructor injection
    public DgAllEmpsAbsMvvmViewModel(@NonNull final DgAllEmpsAbsIDataModel dataModel,
                                     @NonNull final ISchedulerProvider schedulerProvider,
                                     @NonNull final SharedPreferences sharedPreferences,
                                     @NonNull final MCrypt mcrypt,
                                     @NonNull final ConnectivityManager connectivityManager) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
        mSharedPreferences = sharedPreferences;
        mMcrypt = mcrypt;
        mConnectivityManager = connectivityManager;
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

        return mDataModel.getInvoicesFromMysqlServer(encrypted, ds, firx, rokx, drh, dodx, umex, "0");
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
    //emit delete Invoice from Mysql
    public void emitDelInvFromServer(Invoice invx) {

        mObservableInvoiceDelFromServer.onNext(invx);
    }

    @NonNull
    private BehaviorSubject<Invoice> mObservableInvoiceDelFromServer = BehaviorSubject.create();

    @NonNull
    public Observable<List<Invoice>> getMyInvoiceDelFromServer() {

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

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "2";

        Log.d("NewCashLog del fir ", firx);

        return mObservableInvoiceDelFromServer
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx -> mDataModel.getObservableInvoiceDelFromMysql(encrypted, ds, firx, rokx, drh, invx ));
    }

    public void clearObservableInvoiceDelFromServer() {

        mObservableInvoiceDelFromServer = BehaviorSubject.create();

    }
    //end delete Invoice from Mysql


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

    //get edidok invoice from MySql server
    public Observable<List<Invoice>> getEditedInvoiceFromSqlServer(String drh) {

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
        String dokx = mSharedPreferences.getString("edidok", "");

        return mDataModel.getInvoicesFromMysqlServer(encrypted, ds, firx, rokx, drh, dodx, umex, dokx);
    }
    //end get edidok invoice from MySql server

    //emit Observable<Invoice> SaveCashDocRealm
    public void emitMyObservableSaveCashDocRealm(Invoice invx) {
        mObservableSaveCashDocRealm.onNext(invx);
    }

    @NonNull
    private BehaviorSubject<Invoice> mObservableSaveCashDocRealm = BehaviorSubject.create();

    @NonNull
    public Observable<Invoice> getMyObservableSaveCashDocRealm() {

        return mObservableSaveCashDocRealm
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx -> mDataModel.saveCashDocToRealm(invx ));

    }

    public void clearObservableSaveCashDocRealm() {

        mObservableSaveCashDocRealm = BehaviorSubject.create();

    }
    //end emit Observable<Invoice> SaveCashDocRealm

    //emit Observable<IdCompanyKt> control IdCompany
    public void emitMyObservableIdModelCompany(String queryx) {
        //String querys = String.valueOf(queryx);
        mObservableIdModelCompany.onNext(queryx);
    }

    @NonNull
    private BehaviorSubject<String> mObservableIdModelCompany = BehaviorSubject.create();

    @NonNull
    public Observable<List<IdCompanyKt>> getMyObservableIdModelCompany() {

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

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "2";

        Log.d("NewCashLog idc fir ", firx);

        return mObservableIdModelCompany
                .observeOn(mSchedulerProvider.computation())
                .flatMap(queryx -> mDataModel.getObservableIdModelCompany(encrypted, ds, firx, rokx, drh, queryx ));
    }

    public void clearObservableIdModelCompany() {

        mObservableIdModelCompany = BehaviorSubject.create();

    }
    //end emit Observable<IdCompanyKt> control IdCompany

    //emit Observable<CalcVatKt> recount
    public void emitMyObservableRecount(CalcVatKt calcx) {
        //String querys = String.valueOf(queryx);
        mObservableRecount.onNext(calcx);
    }

    @NonNull
    private BehaviorSubject<CalcVatKt> mObservableRecount = BehaviorSubject.create();

    @NonNull
    public Observable<CalcVatKt> getMyObservableRecount() {

        return mObservableRecount
                .observeOn(mSchedulerProvider.computation())
                .flatMap(calcx -> mDataModel.getObservableRecountFromRealm(calcx ));

    }

    public void clearObservableRecount() {

        mObservableRecount = BehaviorSubject.create();

    }
    //end emit Observable<CalcVatKt> recount

    //get uct.pohyby from MySql server
    public Observable<List<Account>> getMyPohybyFromSqlServer(String drh, String drupoh, int interval) {

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
        String uctox = mSharedPreferences.getString("firduct", "");
        long unixTimel = System.currentTimeMillis() / 1000L;

        String drupohx = drupoh;
        if( drh.equals("1")){ drupohx = "11"; }
        if( drh.equals("2")){ drupohx = "12"; }

        return Observable.concatEager(
                mDataModel.getReceiptsExpensesFromRealm(encrypted, ds, firx, rokx, drh, drupohx, uctox)
                        .filter(x -> x.size() > 0 )
                        .filter(x -> ( unixTimel - Long.valueOf(x.get(0).getDatm()) < interval || !isConnectedServer() ) ),
                mDataModel.getReceiptsExpensesFromSql(encrypted, ds, firx, rokx, drh, drupoh, uctox)
                        .observeOn(mSchedulerProvider.ui()) //switch to ui because of Realm is initialize in ui
                        .flatMap(listaccounts -> mDataModel.saveReceiptsExpensesToRealm(listaccounts, drh))
                 ).first();

        //return mDataModel.getReceiptsExpensesFromSql(encrypted, ds, firx, rokx, drh, drupoh, uctox)
        //        .observeOn(mSchedulerProvider.ui()) //switch to ui because of Realm is initialize in ui
        //        .flatMap(listaccounts -> mDataModel.saveReceiptsExpensesToRealm(listaccounts));

        //return mDataModel.getReceiptsExpensesFromRealm(encrypted, ds, firx, rokx, drh, drupoh, uctox);

    }
    //end get get uct.pohyby from MySql server



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

    //save invoices to realm
    public void emitRealmInvoicesToRealm(List<RealmInvoice> invoice) {
        mInvoiceSaveToRealm.onNext(invoice);
    }

    @NonNull
    private BehaviorSubject<List<RealmInvoice>> mInvoiceSaveToRealm = BehaviorSubject.create();

    @NonNull
    public Observable<RealmInvoice> getDataInvoiceSavedToRealm() {
        return mInvoiceSaveToRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(list -> mDataModel.getInvoiceSavingToRealm(list));
    }

    public void clearObservableInvoiceSaveToRealm() {

        mInvoiceSaveToRealm = BehaviorSubject.create();

    }
    //end save invoices to realm

    public void saveDocToPreferences(RealmInvoice invoice) {

        String dokladsaved = invoice.getDok();
        String drhsaved = invoice.getDrh();

        int dokladn = Integer.parseInt(dokladsaved.trim());
        dokladn = dokladn + 1;
        String dokladnew = dokladn + "";

        Log.d("drhsaved ", drhsaved);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if(drhsaved.equals("1")) { editor.putString("odbdok", dokladnew).apply(); }
        if(drhsaved.equals("2")) { editor.putString("doddok", dokladnew).apply(); }
        if(drhsaved.equals("31")) { editor.putString("pokldok", dokladnew).apply(); }
        if(drhsaved.equals("32")) { editor.putString("pokldov", dokladnew).apply(); }
        if(drhsaved.equals("4")) { editor.putString("bankdok", dokladnew).apply(); }
        editor.commit();

    }

    //IdcKtFragment and TypesKtActivity

    //get IDC from MySql server
    public Observable<List<IdCompanyKt>> getMyIdcFromSqlServer(String drh) {

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

        long unixTimel = System.currentTimeMillis() / 1000L;
        int interval = 120;

        return Observable.concatEager(
                mDataModel.getIdCompaniesFromRealm(encrypted, ds, firx, rokx, drh, "0", "9")
                        .filter(x -> x.size() > 0 )
                        .filter(x -> ( unixTimel - Long.valueOf(x.get(0).getDatm()) < interval || !isConnectedServer() ) ),
                mDataModel.getAllIdcFromMysqlServer(encrypted, ds, firx, rokx, drh)
                        .observeOn(mSchedulerProvider.ui()) //switch to ui because of Realm is initialize in ui
                        .flatMap(listaccounts -> mDataModel.saveIdCompaniesToRealm(listaccounts, drh))
        ).first();

    }
    //end get IDC from MySql server


    //recyclerview method for NoSavedDocActivity

    //get no saved doc from MySql server
    public Observable<List<RealmInvoice>> getNoSavedDocFromRealm(String fromact) {

        return mDataModel.getObservableNosavedDocRealm(fromact);
    }
    //end get no saved doc from MySql server

    //emit delete document from Realm
    public void emitDeleteInvoiceFromRealm(RealmInvoice invoicex) {
        mObservableDeleteInvoiceRealm.onNext(invoicex);
    }

    @NonNull
    private BehaviorSubject<RealmInvoice> mObservableDeleteInvoiceRealm = BehaviorSubject.create();

    @NonNull
    public Observable<List<RealmInvoice>> deletedInvoiceFromRealm() {

        return mObservableDeleteInvoiceRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(invoicex -> mDataModel.deleteInvoiceFromRealm(invoicex));
    }

    public void clearDeleteInvoiceRealm() {

        mObservableDeleteInvoiceRealm = BehaviorSubject.create();

    }

    //emit delete all document from Realm
    public void emitDeleteAllInvoicesFromRealm(RealmInvoice invoicex) {
        mObservableDeleteAllInvoicesRealm.onNext(invoicex);
    }

    @NonNull
    private BehaviorSubject<RealmInvoice> mObservableDeleteAllInvoicesRealm = BehaviorSubject.create();

    @NonNull
    public Observable<List<RealmInvoice>> deletedAllInvoicesFromRealm() {

        return mObservableDeleteAllInvoicesRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(invoicex -> mDataModel.deleteAllInvoicesFromRealm(invoicex));
    }

    public void clearDeleteAllInvoicesRealm() {

        mObservableDeleteAllInvoicesRealm = BehaviorSubject.create();

    }


    //emit save Invoice to Mysql
    public void emitMyObservableInvoiceToServer(RealmInvoice invx) {

        mObservableInvoiceToServer.onNext(invx);
    }

    @NonNull
    private BehaviorSubject<RealmInvoice> mObservableInvoiceToServer = BehaviorSubject.create();

    @NonNull
    public Observable<List<Invoice>> getMyObservableInvoiceToServer() {

        Random r = new Random();
        double d = 10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;
        encrypted2 = "";


        try {
            encrypted2 = mMcrypt.bytesToHex( mMcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "2";
        String edidok = mSharedPreferences.getString("edidok", "");

        //Log.d("NewCashLog save fir ", firx);
        Log.d("NewCashedit ", firx);

        return mObservableInvoiceToServer
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx -> mDataModel.getObservableInvoiceToMysql(encrypted2, ds, firx, rokx, drh, invx, edidok ));
    }

    public void clearObservableInvoiceToServer() {

        mObservableInvoiceToServer = BehaviorSubject.create();

    }
    //end save Invoice to Mysql

    //date fun
    //DatePickerDialog
    public DatePickerDialog getDatePickerFromMvvm(String datumx, String posbut, Context context) {

        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        //dd = 38;

        //String datumx = "12.12.2017";

        String delims = "[.]+";
        String[] datumxxx = datumx.split(delims);

        String ddx = datumxxx[0];
        String mmx = datumxxx[1];
        String yyx = datumxxx[2];

        int ddi = Integer.parseInt(ddx);
        int mmi = Integer.parseInt(mmx);
        int yyi = Integer.parseInt(yyx);
        dd=ddi; mm=mmi-1; yy=yyi;

        DatePickerDialog dpd = new DatePickerDialog(context, null, yy, mm, dd);
        //dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Button Neg Text", dpd);
        dpd.setButton(DialogInterface.BUTTON_POSITIVE, posbut, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {

                }
            }

        });


        return dpd;
    }
    //end DatePickerDialog

    // max date of month
    public String getMaxDateOfMonth(String ume) {

        String delims = "[.]+";
        String[] datumxxx = ume.split(delims);
        String mmx = datumxxx[0];
        String rrx = datumxxx[1];
        int mmi = Integer.parseInt(mmx);
        int yyi = Integer.parseInt(rrx);

        Calendar c = Calendar.getInstance();
        c.set(yyi, mmi - 1, 1);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c.getTime());


        return formattedDate;
    }
    //end max date of month


    //control if server is connected

    @NonNull
    public Boolean isConnectedServer(){

        //ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    //end control if server is connected


    //NewIdcActivity
    //emit save Idc to Mysql
    public void emitMyObservableIdcToServer(RealmInvoice invx) {

        mObservableIdcToServer.onNext(invx);
    }

    @NonNull
    private BehaviorSubject<RealmInvoice> mObservableIdcToServer = BehaviorSubject.create();

    @NonNull
    public Observable<List<Invoice>> getMyObservableIdcToServer() {

        Random r = new Random();
        double d = 10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;
        encrypted2 = "";


        try {
            encrypted2 = mMcrypt.bytesToHex( mMcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "2";
        String edidok = mSharedPreferences.getString("edidok", "");

        //Log.d("NewCashLog save fir ", firx);
        Log.d("NewCashedit ", firx);

        return mObservableIdcToServer
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx -> mDataModel.getObservableInvoiceToMysql(encrypted2, ds, firx, rokx, drh, invx, edidok ));
    }

    public void clearObservableIdcSaveToServer() {

        mObservableIdcToServer = BehaviorSubject.create();

    }
    //end save Idc to Mysql

    //save idc to realm
    public void emitRealmIdcToRealm(List<RealmInvoice> invoice) {
        mIdcSaveToRealm.onNext(invoice);
    }

    @NonNull
    private BehaviorSubject<List<RealmInvoice>> mIdcSaveToRealm = BehaviorSubject.create();

    @NonNull
    public Observable<RealmInvoice> getDataIdcSavedToRealm() {
        return mIdcSaveToRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(list -> mDataModel.getInvoiceSavingToRealm(list));
    }

    public void clearObservableIdcSaveToRealm() {

        mIdcSaveToRealm = BehaviorSubject.create();

    }
    //end save idc to realm

}
