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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/*
* How to Implement Load More in #RecyclerView
* by https://medium.com/@programmerasi/how-to-implement-load-more-in-recyclerview-3c6358297f4
*
*/

public class DocSearchActivity  extends BaseListActivity implements DocSearchMvpView {

    private Toolbar toolbar;

    private TextView tvEmptyView, amount;
    private RecyclerView mRecyclerView;
    private DocSearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    protected Handler handler;
    private List<BankItem> searchitems;

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
        amount = (TextView) findViewById(R.id.amount);
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
        presenter.getFirst20SearchItemsFromSql(querystring);

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
        //Log.d("DocSearchMvp ", "student " + studentList.get(0).getName());
    }

    @Override public void setSearchItems(List<BankItem> first20searchitems) {
        //Log.d("DocSearchMvp ", "searchitem " + first20searchitems.get(0).getDok());

        searchitems = new ArrayList<BankItem>();
        searchitems = first20searchitems;
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

                int start = searchitems.size()-1;
                int end = start + 20;

                //presenter.loadNext20SearchItems(start, end);
                presenter.getNext20SearchItemsFromSql(querystring, start, end);

            }
        });

        String amitems="";
        try {
            amitems = first20searchitems.get(0).getBal();
        }catch(IndexOutOfBoundsException e){
            amitems="0";
        }
        amount.setText(amitems);

    }

    @Override public void setNext20SearchItems(List<BankItem> next20searchitems) {
        //Log.d("DocSearchMvp ", "searchitem " + next20searchitems.get(0).getDok());

        //hide bottom progressbar
        searchitems.remove(searchitems.size() - 1);
        mAdapter.notifyItemRemoved(searchitems.size());

        for (int i = 0; i < next20searchitems.size(); i++) {
            searchitems.add(next20searchitems.get(i));
        }
        mAdapter.setLoaded();
        mAdapter.notifyDataSetChanged();
        
        String amitems="";
        try {
            amitems = next20searchitems.get(0).getBal();
        }catch(IndexOutOfBoundsException e){
            amitems="0";
        }
        amount.setText(amitems);
    }

    //option menu
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.docsearch_menu, menu);
        menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        getObservableSearchViewText();

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

    //listener to searchview
    private void getObservableSearchViewText() {

        Observable<String> searchViewChangeStream = createSearchViewTextChangeObservable();

        mDisposable = searchViewChangeStream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        showProgress();
                    }
                })
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String query) {
                        presenter.getForQueryFirst20SearchItemsFromSql(query);
                        return query;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) {
                        //hideProgress();
                        //andrejko presenter.showSearchResult(result);
                    }
                });
    }

    private Observable<String> createSearchViewTextChangeObservable() {
        Observable<String> searchViewTextChangeObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                onQueryTextListener = new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // use this method when query submitted
                        emitter.onNext(query.toString());
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // use this method for auto complete search process
                        emitter.onNext(newText.toString());
                        //andrejko presenter.emitSearchString(newText.toString());
                        return false;
                    }

                };

                searchView.setOnQueryTextListener(onQueryTextListener);

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        searchView.setOnQueryTextListener(null);
                    }
                });
            }
        });

        return searchViewTextChangeObservable
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String query) throws Exception {
                        return query.length() >= 3 || query.equals("");
                    }
                }).debounce(800, TimeUnit.MILLISECONDS);  // add this line
    }



}