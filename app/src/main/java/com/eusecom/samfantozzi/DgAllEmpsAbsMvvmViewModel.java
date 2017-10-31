package com.eusecom.samfantozzi;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.models.Employee;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.samfantozzi.mvvmschedulers.ISchedulerProvider;
import java.util.List;
import rx.Observable;

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

    //@Inject only by Base constructor injection
    public DgAllEmpsAbsMvvmViewModel(@NonNull final DgAllEmpsAbsIDataModel dataModel,
                                     @NonNull final ISchedulerProvider schedulerProvider,
                                     @NonNull final SharedPreferences sharedPreferences) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
        mSharedPreferences = sharedPreferences;
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



    //recyclerview method for ChooseMonthActivity

    //get absences from mock
    public Observable<List<Month>> getMonth() {

        String rokx = mSharedPreferences.getString("rok", "0");

        return mDataModel.getMonthForYear(rokx);
    }
    //end get absences from mock


    //recyclerview method for ChooseCompanyActivity

    //get companies from MySql server
    public Observable<List<CompanyKt>> getMyCompaniesFromServer() {

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus = usuidx + "/" + "abrakadabra";
        String encrypted = "";

        //String userhash = sha1Hash( userx );
        MCrypt mcrypt = new MCrypt();
        	/* Encrypt */
        try {
            encrypted = MCrypt.bytesToHex( mcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        	/* Decrypt */
        //String decrypted = new String( mcrypt.decrypt( encrypted ) );

        return mDataModel.getCompaniesFromMysqlServer(encrypted);
    }
    //end get companies from MySql server


}
