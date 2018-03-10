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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import java.util.List;
import javax.inject.Inject;

/*
* MVP pattern for Bank and Universal Documents List
* by https://github.com/antoniolg/androidmvp
*/

public class BankMvpActivity extends Activity implements BankMvpView, AdapterView.OnItemClickListener {

    private ListView listView;
    private ProgressBar progressBar;
    private BankMvpPresenter presenter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Inject
    AbsServerService mAbsServerService;
    @Inject
    SharedPreferences mSharedPreferences;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SamfantozziApp) getApplication()).dgaeacomponent().inject(this);

        setContentView(R.layout.bankmvp_activity);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        mRecycler = (RecyclerView) findViewById(R.id.rvlist);
        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        presenter = new BankMvpPresenterImpl(this, mSharedPreferences, new BankFindItemsInteractorImpl(mAbsServerService));
    }


    //call presenter
    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemClicked(position);
    }

    @Override protected void onResume() {
        super.onResume();
        //presenter.onResume();
        presenter.onStart();
    }

    @Override protected void onDestroy() {
        //presenter.onDestroy();
        super.onDestroy();
    }

    //View implementation called from presenter
    @Override public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
    }

    @Override public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

    @Override public void setItems(List<String> items) {
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
    }

    @Override public void setInvoiceItems(List<Invoice> items) {
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
    }

    @Override public void setBankItems(AccountItemAdapter mAdapter, List<BankItem> bankitems) {
        //listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        mAdapter.setBankItems(bankitems);
        mRecycler.setAdapter(mAdapter);
    }

    @Override public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //option menu
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bankmvp_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
