package com.eusecom.samfantozzi;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.disposables.Disposable;

/*
* How to Implement Load More in #RecyclerView
* by https://medium.com/@programmerasi/how-to-implement-load-more-in-recyclerview-3c6358297f4
*
*/

public class DocSearchActivity  extends BaseListActivity implements DocSearchMvpView {

    private Toolbar toolbar;

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DocSearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    protected Handler handler;

    //MVP
    private DocSearchMvpPresenter presenter;
    @Inject
    AbsServerService mAbsServerService;
    @Inject
    SharedPreferences mSharedPreferences;

    //searchview
    private SearchView searchView;
    private MenuItem menuItem;
    private SearchView.OnQueryTextListener onQueryTextListener = null;
    SearchManager searchManager;
    protected BankItemSearchEngine mBankItemSearchEngine;
    private Disposable mDisposable;
    private String querystring = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docsearch_activity);

        ((SamfantozziApp) getApplication()).dgaeacomponent().inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        handler = new Handler();

        presenter = (DocSearchMvpPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new DocSearchMvpPresenterImpl(this, mSharedPreferences
                    , new DocSearchInteractorImpl(mAbsServerService));

        }
        presenter.attachView(this);

        getSupportActionBar().setTitle(getString(R.string.docsearch));

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //presenter.loadStudents();
        //presenter.loadSearchItems();
        presenter.onStart();

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override protected void onDestroy() {

        presenter.detachView();
        super.onDestroy();
    }


    @Override public void showProgress() {
        showProgressDialog();
    }

    @Override public void hideProgress() {
        hideProgressDialog();
    }

    @Override public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override public void setStudents(List<DocSearchStudent> studentList) {
        Log.d("DocSearchMvp ", "student " + studentList.get(0).getName());
    }

    @Override public void setSearchItems(List<BankItem> searchitems) {
        Log.d("DocSearchMvp ", "searchitem " + searchitems.get(0).getDok());

        mAdapter = new DocSearchAdapter(searchitems, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        if (searchitems.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        mAdapter.setOnLoadMoreListener(new DocSearchOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                searchitems.add(null);
                mAdapter.notifyItemInserted(searchitems.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        searchitems.remove(searchitems.size() - 1);
                        mAdapter.notifyItemRemoved(searchitems.size());
                        //add items one by one
                        int start = searchitems.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            //searchitems.add(new BankItem(" "," ","100" + i," "," "
                            //        ," "," "," "," "," ","120","pop" + i," "));
                            mAdapter.notifyItemInserted(searchitems.size());
                        }
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

    }

    //option menu
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.docsearch_menu, menu);
        menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        //andrejko getObservableSearchViewText();

        return true;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent is = new Intent(this, SettingsActivity.class);
            startActivity(is);
            return true;
        }

        if (id == R.id.action_setmonth) {

            Intent is = new Intent(this, ChooseMonthActivity.class);
            startActivity(is);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}