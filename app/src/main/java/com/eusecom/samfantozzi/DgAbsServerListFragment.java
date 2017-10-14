package com.eusecom.samfantozzi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.eusecom.samfantozzi.models.Attendance;
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


public class DgAbsServerListFragment extends Fragment {

    public DgAbsServerListFragment() {

    }
    private CompositeDisposable _disposables;
    private AbsServerAsAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private RxBus _rxBus = null;

    private TextWatcher watcher = null;
    private View.OnClickListener onclicklist = null;
    protected EditText mQueryEditText;
    protected Button mSearchButton;
    private ProgressBar mProgressBar;
    private Disposable mDisposable;
    protected AbsServerSearchEngine mAbsServerSearchEngine;

    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    DgAllEmpsAbsMvvmViewModel mViewModel;


    AlertDialog dialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _disposables = new CompositeDisposable();

        _rxBus = ((SamfantozziApp) getActivity().getApplication()).getRxBusSingleton();

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();

        _disposables
                .add(tapEventEmitter.subscribe(event -> {
                    if (event instanceof DgAeaListFragment.ClickFobEvent) {
                        Log.d("DgAeaActivity  ", " fobClick ");
                        String serverx = "AbsServerListFragment fobclick";
                        Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();


                    }


                }));

        _disposables
                .add(tapEventEmitter.publish(stream ->
                        stream.buffer(stream.debounce(1, TimeUnit.SECONDS)))
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(taps -> {
                            ///_showTapCount(taps.size()); OK
                        }));

        _disposables.add(tapEventEmitter.connect());
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_absserver, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.list);
        mRecycler.setHasFixedSize(true);

        mQueryEditText = (EditText) rootView.findViewById(R.id.query_edit_text);
        mSearchButton = (Button) rootView.findViewById(R.id.search_button);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((SamfantozziApp) getActivity().getApplication()).dgaeacomponent().inject(this);

        String umex = mSharedPreferences.getString("ume", "");
        mAdapter = new AbsServerAsAdapter(_rxBus);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        //String serverx = "From fragment " + mSharedPreferences.getString("servername", "");
        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();

        getObservableSearchText();


    }//end of onActivityCreated

    @Override
    public void onDestroy() {
        super.onDestroy();
        _disposables.dispose();
        if( mDisposable != null ) {mDisposable.dispose();}
        mAdapter = new AbsServerAsAdapter(_rxBus);
        _rxBus = null;
        mSubscription.unsubscribe();
        mSubscription.clear();


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
        mSubscription = new CompositeSubscription();

        mSubscription.add(mViewModel.getMyAbsencesFromServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(this::setServerAbsences));

    }

    private void unBind() {
        mSubscription.unsubscribe();
        mSubscription.clear();
        if( mDisposable != null ) {mDisposable.dispose();}
    }


    private void setServerAbsences(@NonNull final List<Attendance> attendances) {
        String serverx = attendances.get(0).getDmna();
        Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();
        if (attendances.isEmpty()) {
            Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
            mAdapter.setAbsserver(Collections.<Attendance>emptyList());
        } else {
            //Log.d("showResultAs ", resultAs.get(0).dmna);
            mAdapter.setAbsserver(attendances);
        }
        nastavResultAs(attendances);
    }

    protected void showResultAs(List<Attendance> resultAs) {

        if (resultAs.isEmpty()) {
            Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
            mAdapter.setAbsserver(Collections.<Attendance>emptyList());
        } else {
            //Log.d("showResultAs ", resultAs.get(0).dmna);
            mAdapter.setAbsserver(resultAs);
        }
    }

    protected void nastavResultAs(List<Attendance> resultAs) {
        mAbsServerSearchEngine = new AbsServerSearchEngine(resultAs);
    }

    public static class ClickFobEvent {}

    protected void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void getObservableSearchText() {
        Observable<String> buttonClickStream = createButtonClickObservable();
        Observable<String> textChangeStream = createTextChangeObservable();

        Observable<String> searchTextObservable = Observable.merge(textChangeStream, buttonClickStream);

        mDisposable = searchTextObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        showProgressBar();
                    }
                })
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(new Function<String, List<Attendance>>() {
                    @Override
                    public List<Attendance> apply(String query) {
                        return mAbsServerSearchEngine.searchModel(query);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Attendance>>() {
                    @Override
                    public void accept(List<Attendance> result) {
                        hideProgressBar();
                        showResultAs(result);
                    }
                });
    }


    private Observable<String> createButtonClickObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                onclicklist = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        emitter.onNext(mQueryEditText.getText().toString());
                    }
                };
                mSearchButton.setOnClickListener(onclicklist);

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        onclicklist = null;
                        mSearchButton.setOnClickListener(null);
                    }
                });
            }
        });
    }

    private Observable<String> createTextChangeObservable() {
        Observable<String> textChangeObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        emitter.onNext(s.toString());
                    }
                };

                mQueryEditText.addTextChangedListener(watcher);

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        mQueryEditText.removeTextChangedListener(watcher);
                    }
                });
            }
        });

        return textChangeObservable
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String query) throws Exception {
                        return query.length() >= 3;
                    }
                }).debounce(1000, TimeUnit.MILLISECONDS);  // add this line
    }



}
