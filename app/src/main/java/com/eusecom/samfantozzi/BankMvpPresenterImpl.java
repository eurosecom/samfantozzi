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

import android.util.Log;
import android.widget.Toast;

import java.util.List;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

public class BankMvpPresenterImpl implements BankMvpPresenter, BankFindItemsInteractor.OnFinishedListener {

    private BankMvpView mainView;
    private BankFindItemsInteractor findItemsInteractor;
    private CompositeSubscription mSubscription;

    public BankMvpPresenterImpl(BankMvpView mainView, BankFindItemsInteractor findItemsInteractor) {
        this.mainView = mainView;
        this.findItemsInteractor = findItemsInteractor;
    }

    @Override public void onResume() {
        if (mainView != null) {
            mainView.showProgress();
        }

        //delayed example
        findItemsInteractor.findItems(this);

    }

    @Override public void onDestroy() {

        mainView = null;
        mSubscription.unsubscribe();
        mSubscription.clear();
    }

    @Override
    public void onStart(){
        if (mainView != null) {
            mainView.showProgress();

        mSubscription = new CompositeSubscription();
        mSubscription.add(findItemsInteractor.findCompanies()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error InvoiceListFragment " + throwable.getMessage());
                    mainView.hideProgress();
                    mainView.showMessage("Server not connected");
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::onFinishedInvoice));
        }
    }


    @Override public void onItemClicked(int position) {
        if (mainView != null) {
            mainView.showMessage(String.format("Position %d clicked", position + 1));
        }
    }


    @Override public void onFinished(List<String> items) {
        if (mainView != null) {
            mainView.setItems(items);
            mainView.hideProgress();
        }
    }

    @Override public void onFinishedInvoice(List<Invoice> invoices) {
        if (mainView != null) {
            //Log.d("BankMvpPresenter ", items.get(0).getDok());
            mainView.setInvoiceItems(invoices);
            mainView.hideProgress();
        }
    }

    public BankMvpView getMainView() {
        return mainView;
    }
}
