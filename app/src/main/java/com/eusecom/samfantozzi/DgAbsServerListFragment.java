package com.eusecom.samfantozzi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.models.Employee;
import com.eusecom.samfantozzi.rxbus.RxBus;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Observable;
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


    }//end of onActivityCreated

    @Override
    public void onDestroy() {
        super.onDestroy();
        _disposables.dispose();
        mAdapter = new AbsServerAsAdapter(_rxBus);
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    }




    public static class ClickFobEvent {}



}
