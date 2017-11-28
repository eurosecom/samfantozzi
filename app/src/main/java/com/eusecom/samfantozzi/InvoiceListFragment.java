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
import android.widget.Toast;
import com.eusecom.samfantozzi.rxbus.RxBus;
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


public class InvoiceListFragment extends Fragment {

    public InvoiceListFragment() {

    }
    private CompositeDisposable _disposables;
    private SupplierAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private ProgressBar mProgressBar;
    private Disposable mDisposable;
    protected SupplierSearchEngine mSupplierSearchEngine;

    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    DgAllEmpsAbsMvvmViewModel mViewModel;

    @Inject
    RxBus _rxBus;

    AlertDialog dialog = null;

    //searchview
    private SearchView searchView;
    private SearchView.OnQueryTextListener onQueryTextListener = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SamfantozziApp) getActivity().getApplication()).dgaeacomponent().inject(this);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_suppliers, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.list);
        mRecycler.setHasFixedSize(true);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String umex = mSharedPreferences.getString("ume", "");
        mAdapter = new SupplierAdapter(_rxBus);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        //String serverx = "From fragment " + mSharedPreferences.getString("servername", "");
        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();


    }//end of onActivityCreated

    @Override
    public void onDestroy() {
        super.onDestroy();
        unBind();
    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
    }

    @Override
    public void onPause() {
        super.onPause();
        unBind();
    }

    private void bind() {

        _disposables = new CompositeDisposable();

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();

        _disposables
                .add(tapEventEmitter.subscribe(event -> {
                    if (event instanceof DgAeaListFragment.ClickFobEvent) {
                        Log.d("SupplierActivity  ", " fobClick ");
                        //String serverx = "AbsServerListFragment fobclick";
                        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();


                    }
                    if (event instanceof Invoice) {

                        String usnamex = ((Invoice) event).getDok();


                        Log.d("SupplierListFragment ",  usnamex);
                        getInvoiceDialog(((Invoice) event));
                        //String serverx = "DgAeaListFragment " + usnamex;
                        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();


                    }

                }));

        _disposables
                .add(tapEventEmitter.publish(stream ->
                        stream.buffer(stream.debounce(1, TimeUnit.SECONDS)))
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(taps -> {
                            ///_showTapCount(taps.size()); OK
                        }));

        _disposables.add(tapEventEmitter.connect());


        mSubscription = new CompositeSubscription();

        showProgressBar();
        mSubscription.add(mViewModel.getMyInvoicesFromSqlServer("2")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e(TAG, "Error Throwable " + throwable.getMessage()))
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setServerInvoices));

        //getObservableSearchViewText();

        ActivityCompat.invalidateOptionsMenu(getActivity());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mSharedPreferences.getString("ume", "") + " "
                + mSharedPreferences.getString("doduce", "") + " " +  getString(R.string.customers));
 }

    private void unBind() {
        mSubscription.unsubscribe();
        mSubscription.clear();
        if( mDisposable != null ) {mDisposable.dispose();}
        _disposables.dispose();

        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setServerInvoices(@NonNull final List<Invoice> invoices) {
        //String serverx = invoices.get(0).getNai();
        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();
        if (invoices.isEmpty()) {
            //Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
            mAdapter.setAbsserver(Collections.<Invoice>emptyList());
        } else {
            //Log.d("showResultAs ", resultAs.get(0).dmna);
            mAdapter.setAbsserver(invoices);
        }
        nastavResultAs(invoices);
        hideProgressBar();
    }

    protected void showResultAs(List<Invoice> resultAs) {

        if (resultAs.isEmpty()) {
            //Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
            mAdapter.setAbsserver(Collections.<Invoice>emptyList());
        } else {
            //Log.d("showResultAs ", resultAs.get(0).dmna);
            mAdapter.setAbsserver(resultAs);
        }
    }

    protected void nastavResultAs(List<Invoice> resultAs) {
        mSupplierSearchEngine = new SupplierSearchEngine(resultAs);
    }

    public static class ClickFobEvent {}

    protected void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    //listener to searchview
    private void getObservableSearchViewText() {

        Observable<String> searchViewChangeStream = createSearchViewTextChangeObservable();

        mDisposable = searchViewChangeStream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        showProgressBar();
                    }
                })
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(new Function<String, List<Invoice>>() {
                    @Override
                    public List<Invoice> apply(String query) {
                        return mSupplierSearchEngine.searchModel(query);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Invoice>>() {
                    @Override
                    public void accept(List<Invoice> result) {
                        hideProgressBar();
                        showResultAs(result);
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
                        //Toast.makeText(getActivity(), "submit " + query, Toast.LENGTH_SHORT).show();
                        emitter.onNext(query.toString());
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // use this method for auto complete search process
                        //Toast.makeText(getActivity(), "change " + newText, Toast.LENGTH_SHORT).show();
                        emitter.onNext(newText.toString());
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


    private void getInvoiceDialog(@NonNull final Invoice invoice) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View textenter = inflater.inflate(R.layout.invoice_edit_dialog, null);

        final EditText valuex = (EditText) textenter.findViewById(R.id.valuex);
        valuex.setText(invoice.getHod());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(textenter).setTitle(getString(R.string.document) + " " + invoice.getDok());
        builder.setPositiveButton(getString(R.string.edit), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();


            }
        })
                .setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog dialog = builder.create();
        builder.show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Retrieve the SearchView and plug it into SearchManager
        inflater.inflate(R.menu.menu_listdoc, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
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

            Intent is = new Intent(getActivity(), ChooseAccountActivity.class);
            Bundle extras = new Bundle();
            extras.putString("fromact", "2");
            is.putExtras(extras);
            startActivity(is);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}