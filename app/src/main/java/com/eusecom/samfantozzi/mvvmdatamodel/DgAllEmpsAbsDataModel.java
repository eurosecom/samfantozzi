package com.eusecom.samfantozzi.mvvmdatamodel;

import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.Observable;
import com.eusecom.samfantozzi.Account;
import com.eusecom.samfantozzi.CalcVatKt;
import com.eusecom.samfantozzi.CompanyKt;
import com.eusecom.samfantozzi.IdCompanyKt;
import com.eusecom.samfantozzi.Invoice;
import com.eusecom.samfantozzi.Month;
import com.eusecom.samfantozzi.R;
import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.models.Employee;
import com.eusecom.samfantozzi.realm.RealmEmployee;
import com.eusecom.samfantozzi.realm.RealmInvoice;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.eusecom.samfantozzi.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

public class DgAllEmpsAbsDataModel implements DgAllEmpsAbsIDataModel {

    DatabaseReference mFirebaseDatabase;
    AbsServerService mAbsServerService;
    Resources mResources;
    Realm mRealm;

    public DgAllEmpsAbsDataModel(@NonNull final DatabaseReference databaseReference,
                                 @NonNull final AbsServerService absServerService,
                                 @NonNull final Resources resources,
                                 @NonNull final Realm realm) {
        mFirebaseDatabase = databaseReference;
        mAbsServerService = absServerService;
        mResources = resources;
        mRealm = realm;
    }


    //recyclerview datamodel for DgAeaActivity


    @NonNull
    @Override
    public Observable<List<Attendance>> getAbsencesFromMysqlServer(String fromfir) {

        return mAbsServerService.getAbsServer(fromfir);


    }

    @NonNull
    @Override
    public Observable<List<Attendance>> getAbsencesFromMock(String fromfir) {

        return Observable.just(getMockAttendance());

    }


    @NonNull
    @Override
    public Observable<List<Employee>> getObservableFBusersRealmEmployee(String usicox, String usuid, int lenmoje) {

        Query usersQuery = mFirebaseDatabase.child("users").orderByChild("usico").equalTo(usicox);

        return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery)
                .flatMap(dataSnapshot ->{
                    List<Employee> blogPostEntities = new ArrayList<>();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String keys = childDataSnapshot.getKey();
                        //System.out.println("keys " + keys);
                        Employee resultx = childDataSnapshot.getValue(Employee.class);
                        resultx.setKeyf(keys);
                        blogPostEntities.add(resultx);
                    }
                    return Observable.just(blogPostEntities);
                });

    }


    public List<Attendance> getMockAttendance() {


        List<Attendance> mockAttendance = new ArrayList<>();

        Attendance newAttendance = new Attendance("44551142", "usid", "10.2017", "506",
                "Mock Dovolena", "1506549600", "1506549600", "2",
                "4", "0", "0", "1506549600", "1", "andrejd" );

        mockAttendance.add(newAttendance);

        return mockAttendance;

    }


    //recyclerview method for ChooseMonthActivity

    public Observable<List<Month>> getMonthForYear(String rokx) {

        List<Month> mymonths = new ArrayList<>();
        Month newmonth = new Month(mResources.getString(R.string.january), "01." + rokx, "1");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.february), "02." + rokx, "2");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.march), "03." + rokx, "3");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.april), "04." + rokx, "4");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.may), "05." + rokx, "5");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.june), "06." + rokx, "6");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.july), "07." + rokx, "7");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.august), "08." + rokx, "8");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.september), "09." + rokx, "9");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.october), "10." + rokx, "10");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.november), "11." + rokx, "11");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.december), "12." + rokx, "12");
        mymonths.add(newmonth);

        return Observable.just(mymonths);
    }


    //recyclerview method for ChooseCompanyuActivity

    @Override
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String userhash, String userid) {

        return mAbsServerService.getCompaniesFromServer(userhash, userid);

    }

    //recyclerview method for ChooseAccountActivity
    @Override
    public Observable<List<Account>> getAccountsFromMysqlServer(String userhash, String userid, String fromfir
            , String vyb_rok, String drh) {

        return mAbsServerService.getAccountsFromSqlServer(userhash, userid, fromfir, vyb_rok, drh);
        //return mAbsServerService.controlIdCompanyOnSqlServer(userhash, userid, fromfir, vyb_rok, drh, "xxx");

    }


    @Override
    public Observable<List<Account>> getAccounts(String rokx) {

        List<Account> mymonths = new ArrayList<>();
        Account newmonth = new Account("Dodavatelia", "32100", "830001", "0", "2", true);
        mymonths.add(newmonth);


        return Observable.just(mymonths);
    }

    //recyclerview datamodel for SupplierListActivity

    @NonNull
    @Override
    public Observable<List<Attendance>> getInvoicesFromServer(String fromfir) {

        return mAbsServerService.getInvoicesFromServer(fromfir);

    }

    @Override
    public Observable<List<Invoice>> getInvoicesFromMysqlServer(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex) {

        return mAbsServerService.getInvoicesFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, umex);

    }

    //recyclerview method for CashListKtActivity
    @NonNull
    @Override
    public Observable<List<Attendance>> getObservableAbsencesFromFB(@NonNull final String dokx, @NonNull final String umex
            , @NonNull final String usicox, String usuid, String ustype) {

        int lenmoje=1;
        if (ustype.equals("99")) {
            lenmoje=0;
        }else{

        }
        String umexy = umex;
        if (dokx.equals("0")) {
            umexy="0";
        }
        Query usersQuery = mFirebaseDatabase.child("company-absences").child(usicox).orderByChild("ume").equalTo(umexy);
        if( lenmoje == 1 ){
            usersQuery = mFirebaseDatabase.child("user-absences").child(usuid).orderByChild("ume").equalTo(umexy);
        }

        return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery)
                .flatMap(dataSnapshot ->{
                    List<Attendance> blogPostEntities = new ArrayList<>();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String keys = childDataSnapshot.getKey();
                        //System.out.println("keys " + keys);
                        Attendance resultx = childDataSnapshot.getValue(Attendance.class);
                        resultx.setRok(keys);
                        blogPostEntities.add(resultx);
                    }
                    return Observable.just(blogPostEntities);
                });

    }

    @NonNull
    @Override
    public Observable<Uri> getObservableUriDocPdf(Invoice invx, @NonNull final String firx
            , @NonNull final String rokx, @NonNull final String serverx, @NonNull final String adresx
            , String encrypted) {

        String drupoh = "1";
        Log.d("dokx ", invx.getDok());
        return Observable.just(Uri.parse("http://" + serverx +
                "/ucto/vspk_pdf.php?cislo_dok=" + invx.getDok() + "&hladaj_dok=" + invx.getDok()
                + "&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&copern=20&drupoh="+ drupoh + "&page=1&serverx="
                + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1" ));
    }

    @NonNull
    @Override
    public Observable<String> getObservableCashListQuery(@NonNull final String queryx) {

        return Observable.just(queryx);
    }

    //method for NewCashDocKtActivity
    @NonNull
    public Observable<Boolean> getObservableIdCompany(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String queryx){

        return Observable.just(true);
        //return mAbsServerService.controlIdCompanyOnSqlServer(userhash, userid, fromfir, vyb_rok, drh, queryx);
    }

    @NonNull
    public Observable<List<IdCompanyKt>> getObservableIdModelCompany(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String queryx){

        List<IdCompanyKt> myidc = new ArrayList<>();
        IdCompanyKt newidc = new IdCompanyKt("31414466", "", "", "Firma xyz", "ulixyz",
                "Mesto", "", "", true);
        myidc.add(newidc);

        //Log.d("userhash ", userhash);
        //System.out.println("userid " + userid);
        //System.out.println("fromfir " + fromfir);
        //System.out.println("vyb_rok " + vyb_rok);
        //System.out.println("drh " + drh);
        Log.d("NewCashLog data queryx ", queryx);

        //return Observable.just(myidc);
        return mAbsServerService.controlIdCompanyOnSqlServer(userhash, userid, fromfir, vyb_rok, drh, queryx);
    }

    @Override
    public Observable<List<Account>> getReceiptsExpensesFromSql(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String drupoh, String ucto) {

        return mAbsServerService.getReceiptExpensesFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, drupoh, ucto);

    }

    @NonNull
    @Override
    public Observable<CalcVatKt> getObservableRecountFromRealm(CalcVatKt calcx) {

        calcx.setSumnod();
        return Observable.just(calcx);
    }

    @NonNull
    @Override
    public Observable<Invoice> saveCashDocToRealm(Invoice invx) {

        return Observable.just(invx);
    }

    @NonNull
    @Override
    public Observable<RealmInvoice> getInvoiceSavingToRealm(@NonNull final List<RealmInvoice> invoices) {

        //does exist invoice in Realm?
        RealmInvoice invoiceexists = existRealmInvoice( invoices );

        if(invoiceexists != null){
            //System.out.println("existRealmInvoice " + true);
            deleteRealmInvoiceData( invoices );
        }else{
            //System.out.println("existRealmInvoice " + false);
        }
        //save to realm and get String OK or ERROR
        setRealmInvoiceData( invoices );

        return Observable.just(invoices.get(0));

    }

    public RealmInvoice existRealmInvoice(@NonNull final List<RealmInvoice> invoices) {

        String dokx = invoices.get(0).getDok();
        return mRealm.where(RealmInvoice.class).equalTo("dok", dokx).findFirst();
    }

    private void setRealmInvoiceData(@NonNull final List<RealmInvoice> invoices) {

        //clear all items in table
        //mRealm.beginTransaction();
        //mRealm.clear(RealmInvoice.class);
        //mRealm.commitTransaction();


        for (RealmInvoice b : invoices) {
            // Persist your data easily
            mRealm.beginTransaction();
            mRealm.copyToRealm(b);
            mRealm.commitTransaction();
        }

    }

    private void deleteRealmInvoiceData(@NonNull final List<RealmInvoice> invoices) {

        String dokx = invoices.get(0).getDok();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmInvoice> result = realm.where(RealmInvoice.class).equalTo("dok", dokx).findAll();
                result.clear();
            }
        });

    }


    //method for TypesKtActivity
    @Override
    public Observable<List<IdCompanyKt>> getAllIdcFromMysqlServer(String userhash, String userid, String fromfir
            , String vyb_rok, String drh) {

        return mAbsServerService.getAllIdCompanyOnSqlServer(userhash, userid, fromfir, vyb_rok, drh, "xxx");

    }



    //recyclerview method for NoSavedDocActivity

    @NonNull
    @Override
    public Observable<List<RealmInvoice>> getObservableNosavedDocRealm(String fromact) {

        List<RealmInvoice> results = null;
        String drhx = fromact;
        if (fromact.equals("3")) {
            results = mRealm.where(RealmInvoice.class).equalTo("saved", "false").equalTo("drh", "31")
                    .or().equalTo("saved", "false").equalTo("drh", "32").findAll();
        }else{
            results = mRealm.where(RealmInvoice.class).equalTo("saved", "false").equalTo("drh", drhx).findAll();
        }


        return Observable.just(results);
    }

    @NonNull
    @Override
    public Observable<List<RealmInvoice>> deleteInvoiceFromRealm(RealmInvoice invoicex) {

        String docx = invoicex.getDok();
        String fromact = invoicex.getDrh();
        //System.out.println("deleteInvoiceFromRealm " + docx);

        mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RealmInvoice> result = realm.where(RealmInvoice.class).equalTo("drh", fromact).equalTo("dok", docx).findAll();
                    result.clear();
                }
         });

        List<RealmInvoice> results = null;
        String drhx = fromact;
        if (fromact.equals("31") || fromact.equals("32")) {
            results = mRealm.where(RealmInvoice.class).equalTo("saved", "false").equalTo("drh", "31")
                    .or().equalTo("saved", "false").equalTo("drh", "32").findAll();
        }else{
            results = mRealm.where(RealmInvoice.class).equalTo("saved", "false").equalTo("drh", drhx).findAll();
        }

        return Observable.just(results);
    }

    @NonNull
    @Override
    public Observable<List<RealmInvoice>> deleteAllInvoicesFromRealm(RealmInvoice invoicex) {

        String docx = invoicex.getDok();
        String fromact = invoicex.getDrh();
        //System.out.println("fromact " + fromact);

        mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RealmInvoice> result = realm.where(RealmInvoice.class).equalTo("drh", fromact).findAll();
                    result.clear();
                }
         });


        List<RealmInvoice> results = null;
        String drhx = fromact;
        if (fromact.equals("31") || fromact.equals("32")) {
            results = mRealm.where(RealmInvoice.class).equalTo("saved", "false").equalTo("drh", "31")
                    .or().equalTo("saved", "false").equalTo("drh", "32").findAll();
        }else{
            results = mRealm.where(RealmInvoice.class).equalTo("saved", "false").equalTo("drh", drhx).findAll();
        }

        return Observable.just(results);
    }

    @NonNull
    public Observable<List<IdCompanyKt>> getObservableInvoiceToMysql(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, RealmInvoice invx){

        List<IdCompanyKt> myidc = new ArrayList<>();
        IdCompanyKt newidc = new IdCompanyKt("31414466", "", "", "Firma xyz", "ulixyz",
                "Mesto", "", "", true);
        myidc.add(newidc);

        //Log.d("userhash ", userhash);
        System.out.println("invx.dok " + invx.getDok());
        System.out.println("invx.hod " + invx.getHod());

        //data class Invoice(var drh : String, var uce : String, var dok : String, var ico: String, var nai: String
//        , var fak: String, var ksy: String, var ssy: String
//        , var ume: String, var dat: String, var daz: String, var das: String, var poz: String
//        , var hod: String, var zk0: String, var zk1: String, var dn1: String, var zk2: String, var dn2: String
//        , var saved: Boolean, var datm: Long, var uzid: String)

        String invxstring = "{" +
                "  \"drh\":" + "\"" + invx.getDrh() + "\"" +
                ", \"uce\":" + "\"" + invx.getUce() + "\"" +
                ", \"dok\":" + "\"" + invx.getDok() + "\"" +
                ", \"ico\":" + "\"" + invx.getIco() + "\"" +
                ", \"nai\":" + "\"" + invx.getNai() + "\"" +
                ", \"kto\":" + "\"" + invx.getKto() + "\"" +
                ", \"fak\":" + "\"" + invx.getFak() + "\"" +
                ", \"ksy\":" + "\"" + invx.getKsy() + "\"" +
                ", \"ssy\":" + "\"" + invx.getSsy() + "\"" +
                ", \"ume\":" + "\"" + invx.getUme() + "\"" +
                ", \"dat\":" + "\"" + invx.getDat() + "\"" +
                ", \"daz\":" + "\"" + invx.getDaz() + "\"" +
                ", \"das\":" + "\"" + invx.getDas() + "\"" +
                ", \"poz\":" + "\"" + invx.getPoz() + "\"" +
                ", \"zk0\":" + "\"" + invx.getZk0() + "\"" +
                ", \"zk1\":" + "\"" + invx.getZk1() + "\"" +
                ", \"dn1\":" + "\"" + invx.getDn1() + "\"" +
                ", \"zk2\":" + "\"" + invx.getZk2() + "\"" +
                ", \"dn2\":" + "\"" + invx.getDn2() + "\"" +
                ", \"saved\":" + "\"" + invx.getSaved() + "\"" +
                ", \"datm\":" + "\"" + invx.getDatm() + "\"" +
                ", \"uzid\":" + "\"" + invx.getUzid() + "\"" +
                " }";

        System.out.println("invxstring " + invxstring);

        //GET API
        //return mAbsServerService.saveInvoiceToMysql(userhash, userid, fromfir, vyb_rok, drh, invx);
        //POST API
        return mAbsServerService.saveInvoiceToMysqlPost(userhash, userid, fromfir, vyb_rok, drh, invxstring);
    }






}
