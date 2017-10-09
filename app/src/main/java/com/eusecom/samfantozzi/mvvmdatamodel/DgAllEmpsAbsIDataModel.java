package com.eusecom.samfantozzi.mvvmdatamodel;

import android.support.annotation.NonNull;

import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.models.Employee;
import java.util.List;
import rx.Observable;

public interface DgAllEmpsAbsIDataModel {

    //recyclerview methods for DgAeaActivity

    @NonNull
    Observable<List<Employee>> getObservableFBusersRealmEmployee(String usicox, String usuid, int lenmoje);

    @NonNull
    public Observable<List<Attendance>> getAbsencesFromMysqlServer(String fromfir);


}
