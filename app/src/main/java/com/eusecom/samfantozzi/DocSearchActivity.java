package com.eusecom.samfantozzi;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.eusecom.samfantozzi.retrofit.AbsServerService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/*
* How to Implement Load More in #RecyclerView
* by https://medium.com/@programmerasi/how-to-implement-load-more-in-recyclerview-3c6358297f4
*
*/

public class DocSearchActivity  extends AppCompatActivity implements DocSearchMvpView {

    private Toolbar toolbar;

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DocSearchDataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<DocSearchStudent> studentList;

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
        studentList = new ArrayList<DocSearchStudent>();
        handler = new Handler();

        presenter = (DocSearchMvpPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new DocSearchMvpPresenterImpl(this, mSharedPreferences
                    , new DocSearchInteractorImpl(mAbsServerService));

        }
        presenter.attachView(this);

        getSupportActionBar().setTitle(getString(R.string.docsearch));

        loadData();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);
        // create an Object for Adapter
        mAdapter = new DocSearchDataAdapter(studentList, mRecyclerView);
        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.notifyDataSetChanged();

        if (studentList.isEmpty()) {
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
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());
                        //add items one by one
                        int start = studentList.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            studentList.add(new DocSearchStudent("Student " + i, "AndroidStudent" + i + "@gmail.com"));
                            mAdapter.notifyItemInserted(studentList.size());
                        }
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override protected void onDestroy() {

        presenter.detachView();
        super.onDestroy();
    }


    // load initial data
    private void loadData() {

        for (int i = 1; i <= 20; i++) {
            studentList.add(new DocSearchStudent("Student " + i, "androidstudent" + i + "@gmail.com"));

        }

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