package com.eusecom.samfantozzi.mvvmdatamodel;

import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import com.eusecom.samfantozzi.Account;
import com.eusecom.samfantozzi.CompanyKt;
import com.eusecom.samfantozzi.Invoice;
import com.eusecom.samfantozzi.Month;
import com.eusecom.samfantozzi.R;
import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.models.Employee;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.eusecom.samfantozzi.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class DgAllEmpsAbsDataModel implements DgAllEmpsAbsIDataModel {

    DatabaseReference mFirebaseDatabase;
    AbsServerService mAbsServerService;
    Resources mResources;

    public DgAllEmpsAbsDataModel(@NonNull final DatabaseReference databaseReference,
                                 @NonNull final AbsServerService absServerService,
                                 @NonNull final Resources resources) {
        mFirebaseDatabase = databaseReference;
        mAbsServerService = absServerService;
        mResources = resources;
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

    }

    @Override
    public Observable<List<Account>> getAccounts(String rokx) {

        List<Account> mymonths = new ArrayList<>();
        Account newmonth = new Account("Dodavatelia", "32100", "830001", "0", "2");
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



}
