/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.eusecom.samfantozzi;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.models.BankItemList;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import java.util.Arrays;
import java.util.List;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;


public class BankFindItemsInteractorImpl implements BankFindItemsInteractor {

    AbsServerService mAbsServerService;

    public BankFindItemsInteractorImpl (@NonNull final AbsServerService absServerService ) {
        mAbsServerService = absServerService;
    }

    @Override public Observable<List<Invoice>> findCompanies() {

        return mAbsServerService.getExample("301");

    }

    //find Bank Docs from Mysql
    @Override public Observable<List<BankItem>> findBankItems(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String uce, String ume, String dokx) {

        return mAbsServerService.getBankItemsFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, uce, ume, dokx);

    }
    //end find Bank Docs from Mysql

    //find BankItemsList rom Mysql
    @Override public Observable<BankItemList> findBankItemsWithBalance(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String uce, String ume, String dokx) {

        return mAbsServerService.getBankItemsFromSqlServerWithBalance(userhash, userid, fromfir, vyb_rok, drh, uce, ume, dokx);

    }
    //end find BankItemsList from Mysql

    //delete Bank Doc from Mysql
    @Override public Observable<BankItemList> getMyDocDelFromServer(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String uce, String ume, String dokx) {

        System.out.println("invxstring userhash " + userhash);
        System.out.println("invxstring userid " + userid);
        System.out.println("invxstring fromfir " + fromfir);
        System.out.println("invxstring vyb_rok " + vyb_rok);
        System.out.println("invxstring drh " + drh);
        System.out.println("invxstring dok " + dokx);

        return mAbsServerService.deleteBankDocFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, uce, ume, dokx);

    }
    //end delete Bank Doc from Mysql

    @Override public void findItems(final OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                listener.onFinished(createArrayList());
            }
        }, 2000);
    }

    private List<String> createArrayList() {
        return Arrays.asList(
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 6",
                "Item 7",
                "Item 8",
                "Item 9",
                "Item 10"
        );
    }
}
