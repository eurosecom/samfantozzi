package com.eusecom.samfantozzi;

import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.samfantozzi.realm.RealmDomain;
import com.eusecom.samfantozzi.realm.RealmInvoice;
import java.util.List;
import rx.Observable;

/**
 * ViewModel implementation that logs every count increment.
 */
public class DomainsViewModel extends DomainsBaseViewModel {

    private final LoggingClickInterceptor loggingInterceptor;
    DgAllEmpsAbsIDataModel mDataModel;

    public DomainsViewModel(LoggingClickInterceptor loggingInterceptor, DgAllEmpsAbsIDataModel dataModel ) {
        this.loggingInterceptor = loggingInterceptor;
        this.mDataModel = dataModel;
    }

    @Override
    public void setCount(int count) {
        super.setCount(count);
        loggingInterceptor.intercept(count);
    }

    //get no saved doc from Realm
    public Observable<List<RealmInvoice>> getNoSavedDocFromRealm(String fromact) {

        return mDataModel.getObservableNosavedDocRealm(fromact);
    }
    //end get no saved doc from Realm

    //get no saved doc from Realm
    public Observable<List<RealmDomain>> getSavedDomainFromRealm() {

        return mDataModel.getDomainsFromRealm();
    }
    //end get no saved doc from Realm

}
