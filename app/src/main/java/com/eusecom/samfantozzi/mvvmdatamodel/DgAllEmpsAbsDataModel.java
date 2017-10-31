package com.eusecom.samfantozzi.mvvmdatamodel;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import com.eusecom.samfantozzi.CompanyKt;
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
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String userhash) {

        return mAbsServerService.getCompaniesFromServer(userhash);


    }


}
