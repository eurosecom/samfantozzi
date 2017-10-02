package com.eusecom.samfantozzi.mvvmdatamodel;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import com.eusecom.samfantozzi.models.Employee;
import com.eusecom.samfantozzi.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class DgAllEmpsAbsDataModel implements DgAllEmpsAbsIDataModel {

    DatabaseReference mFirebaseDatabase;

    public DgAllEmpsAbsDataModel(@NonNull final DatabaseReference databaseReference) {
        mFirebaseDatabase = databaseReference;
    }


    //recyclerview datamodel for DgAeaActivity

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




}
