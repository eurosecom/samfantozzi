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

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.models.BankItemList;
import com.eusecom.samfantozzi.models.GeneralDocPresenterState;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

public class GeneralDocMvpPresenterImpl implements GeneralDocMvpPresenter, GeneralDocInteractor.OnFinishedListener {

    private GeneralDocMvpView mainView;
    private GeneralDocInteractor genDocInteractor;
    private SharedPreferences mSharedPreferences;
    private String searchQuery = "";
    private CompositeSubscription mSubscription;

    public GeneralDocMvpPresenterImpl(GeneralDocMvpView mainView, SharedPreferences sharedPreferences,
                                      GeneralDocInteractor generalDocInteractor) {
        this.mainView = mainView;
        this.mSharedPreferences = sharedPreferences;
        this.genDocInteractor = generalDocInteractor;
    }

    @Override
    public void attachView(GeneralDocMvpView view, GeneralDocPresenterState state) {
        this.mainView = view;

        Log.d("MvpPresenter ", "attachView " + searchQuery);
        if (mainView != null) {
            mainView.setQueryToSearch(state.getQuerystring());
        }
    }

    @Override
    public void detachView() {
        this.mainView = null;
        Log.d("MvpPresenter ", "detachView " + searchQuery);
    }

    //emit BankList search query
    public void emitSearchString(String queryx) {

        Log.d("MvpPresenter ", "emit " + searchQuery);
        searchQuery = queryx;

    }

    //get Serialized Presenter
    public GeneralDocPresenterState getSerializedPresenter() {

        GeneralDocPresenterState state = new GeneralDocPresenterState(searchQuery, "", "");

        return state;
    }

    @Override
    public void onFinished(List<String> items) {

    }

    @Override
    public void onStart() {

        if(mainView != null )
        {
            mainView.showProgress();

            Random r = new Random();
            double d = 10.0 + r.nextDouble() * 20.0;
            String ds = String.valueOf(d);

            String usuidx = mSharedPreferences.getString("usuid", "");
            String userxplus = ds + "/" + usuidx + "/" + ds;

            MCrypt mcrypt = new MCrypt();
            String encrypted2 = "";
            try {
                encrypted2 = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            String firx = mSharedPreferences.getString("fir", "");
            String rokx = mSharedPreferences.getString("rok", "");
            String drh = "5";
            String dodx = mSharedPreferences.getString("doduce", "");
            String umex = mSharedPreferences.getString("ume", "");
            String edidok = mSharedPreferences.getString("edidok", "");

            mSubscription = new CompositeSubscription();
            mSubscription.add(genDocInteractor.findGeneralItemsWithBalance(encrypted2, ds, firx, rokx, drh, dodx, umex, edidok)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError(throwable -> { Log.e(TAG, "Error GenDocMvpPresenter " + throwable.getMessage());
                        mainView.hideProgress();
                        mainView.showMessage("Server not connected");
                    })
                    .onErrorResumeNext(throwable -> empty())
                    .subscribe(this::onFinishedGenItemList));

        }

    }

    public void onFinishedGenItemList(BankItemList bankitems) {
        //Log.d("Presenter bankitems", bankitems.getBalance());
        if (mainView != null) {
            //Log.d("MvpPresenter ", bankitems.getBankitem().get(0).getDok());
            mainView.setGeneralItems(bankitems.getBankitem());
            //mainView.setBalance(bankitems.getBalance());
            mainView.hideProgress();
        }
    }


}