package com.eusecom.samfantozzi.mvvmdatamodel;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;

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

    public DgAllEmpsAbsDataModel(@NonNull final DatabaseReference databaseReference,
                                 @NonNull final AbsServerService absServerService) {
        mFirebaseDatabase = databaseReference;
        mAbsServerService = absServerService;
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



}
