package com.eusecom.samfantozzi;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.models.GeneralDocPresenterState;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.eusecom.samfantozzi.rxbus.RxBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static android.content.Context.SEARCH_SERVICE;
import static rx.Observable.empty;


public class GeneralDocFragment extends Fragment implements GeneralDocMvpView  {

    public GeneralDocFragment() {

    }

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    AbsServerService mAbsServerService;

    private GeneralDocMvpPresenter presenter;

    //searchview
    private SearchView searchView;
    private MenuItem menuItem;
    private SearchView.OnQueryTextListener onQueryTextListener = null;
    SearchManager searchManager;
    private String querystring = "";
    protected BankItemSearchEngine mBankItemSearchEngine;
    private Disposable mDisposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SamfantozziApp) getActivity().getApplication()).dgaeacomponent().inject(this);
        setHasOptionsMenu(true);

        // restore previous status data if available
        GeneralDocPresenterState state = new GeneralDocPresenterState("","","");
        if(savedInstanceState != null) {
            state =
                    (GeneralDocPresenterState)savedInstanceState.getSerializable("PRESENTERSTATE");
            Log.d("MvpPresenter onGet ", "state " + state.getQuerystring());
        }
        if (presenter == null) {
            presenter = new GeneralDocMvpPresenterImpl(this, mSharedPreferences
                    , new GeneralDocInteractorImpl(mAbsServerService));

        }
        presenter.attachView(this, state);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putSerializable("PRESENTERSTATE", presenter.getSerializedPresenter());
        Log.d("MvpPresenter onSave ", "state " + presenter.getSerializedPresenter().getQuerystring());

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_generaldocs, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.list);
        mRecycler.setHasFixedSize(true);


        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String umex = mSharedPreferences.getString("ume", "");
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        //mRecycler.setAdapter(mAdapter);

        mBankItemSearchEngine = new BankItemSearchEngine(Collections.<BankItem>emptyList());

    }//end of onActivityCreated

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        mDisposable.dispose();

    }

    @Override public void onResume() {
        super.onResume();
        bind();
    }


    private void bind() {

        ActivityCompat.invalidateOptionsMenu(getActivity());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mSharedPreferences.getString("ume", "") + " "
                +  getString(R.string.generaldoc));

    }

    @Override public void setQueryToSearch(String querystringx) {

        //querystringx = "20";
        Log.d("MvpPresenter query ", "in View " + querystringx);
        if( querystringx.equals("")){
        }else {
            querystring = querystringx;

        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Retrieve the SearchView and plug it into SearchManager
        inflater.inflate(R.menu.menu_listdoc, menu);
        inflater.inflate(R.menu.bankmvp_menu, menu);
        menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        if( querystring.equals("")){

        }else {
            searchView.setIconified(false);
            searchView.setQuery(querystring, false);
            menuItem.setVisible(true);
            presenter.emitSearchString(querystring);
        }

        getObservableSearchViewText();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent is = new Intent(getActivity(), SettingsActivity.class);
            startActivity(is);
            return true;
        }

        if (id == R.id.action_setmonth) {

            Intent is = new Intent(getActivity(), ChooseMonthActivity.class);
            startActivity(is);
            return true;
        }

        if (id == R.id.action_setaccount) {


            return true;
        }

        if (id == R.id.action_nosaveddoc) {

            Intent is = new Intent(getActivity(), NoSavedDocActivity.class);
            Bundle extras = new Bundle();
            extras.putString("fromact", "3");
            is.putExtras(extras);
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
                        //andrejdko showProgress();
                    }
                })
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(new Function<String, List<BankItem>>() {
                    @Override
                    public List<BankItem> apply(String query) {
                        return mBankItemSearchEngine.searchModel(query);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BankItem>>() {
                    @Override
                    public void accept(List<BankItem> result) {
                        //andrejko hideProgress();
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
                        //Toast.makeText(getActivity(), "change " + newText, Toast.LENGTH_SHORT).show();
                        emitter.onNext(newText.toString());
                        presenter.emitSearchString(newText.toString());
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
                }).debounce(300, TimeUnit.MILLISECONDS);  // add this line
    }


}
