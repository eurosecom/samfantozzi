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
import android.os.Handler;
import android.util.Log;

import com.eusecom.samfantozzi.models.BankItem;

import java.util.Arrays;
import java.util.List;
import rx.subscriptions.CompositeSubscription;

public class DocSearchMvpPresenterImpl implements DocSearchMvpPresenter, DocSearchInteractor.OnFinishedListener  {

    private DocSearchMvpView mainView;
    private DocSearchInteractor docSearchInteractor;
    private CompositeSubscription mSubscription;
    private SharedPreferences mSharedPreferences;
    private String searchQuery = "";


    public DocSearchMvpPresenterImpl(DocSearchMvpView mainView, SharedPreferences sharedPreferences,
                                DocSearchInteractor mDocSearchInteractor) {
        this.mainView = mainView;
        this.mSharedPreferences = sharedPreferences;
        this.docSearchInteractor = mDocSearchInteractor;
    }

    @Override
    public void attachView(DocSearchMvpView view) {
        this.mainView = view;

        Log.d("DocSearchMvpPresenter ", "attachView " + searchQuery);
        if (mainView != null) {
            //andrejko mainView.setQueryToSearch(searchQuery);
        }
    }

    @Override
    public void detachView() {
        this.mainView = null;
        Log.d("DocSearchMvpPresenter ", "detachView " + searchQuery);
    }


    //load students list
    @Override
    public void loadStudents() {
        if (mainView != null) {
            mainView.showProgress();
        }
        docSearchInteractor.loadStudentsList(this);
    }

    @Override public void onFinishedStudents(List<DocSearchStudent> items) {
        if (mainView != null) {
            mainView.setStudents(items);
            mainView.hideProgress();
        }
    }


    //load bankitems list
    @Override
    public void loadSearchItems() {
        if (mainView != null) {
            mainView.showProgress();
        }
        docSearchInteractor.loadSearchItemsList(this);
    }

    @Override public void onFinishedSearchItems(List<BankItem> items) {
        if (mainView != null) {
           mainView.setSearchItems(items);
           mainView.hideProgress();
        }
    }


}
