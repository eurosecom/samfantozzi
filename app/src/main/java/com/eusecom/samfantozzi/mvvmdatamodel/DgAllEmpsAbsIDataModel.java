package com.eusecom.samfantozzi.mvvmdatamodel;

import android.support.annotation.NonNull;

import com.eusecom.samfantozzi.Account;
import com.eusecom.samfantozzi.CompanyKt;
import com.eusecom.samfantozzi.Invoice;
import com.eusecom.samfantozzi.Month;
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

    @NonNull
    public Observable<List<Attendance>> getAbsencesFromMock(String fromfir);

    //recyclerview method for ChooseMonthActivity
    @NonNull
    public Observable<List<Month>> getMonthForYear(String rokx);

    //recyclerview method for ChooseAccountActivity
    @NonNull
    public Observable<List<Invoice>> getAccountsFromMysqlServer(String userhash, String userid, String fromfir
            , String vyb_rok, String drh);

    @NonNull
    public Observable<List<Account>> getAccounts(String rokx);

    //recyclerview method for ChooseCompanyActivity
    @NonNull
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String userhash, String userid);

    //recyclerview method for SupplierListActivity
    @NonNull
    public Observable<List<Attendance>> getInvoicesFromServer(String fromfir);

    @NonNull
    public Observable<List<Invoice>> getInvoicesFromMysqlServer(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex);

}
