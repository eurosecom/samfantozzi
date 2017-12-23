package com.eusecom.samfantozzi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.eusecom.samfantozzi.rxbus.RxBus;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import butterknife.Bind;
import butterknife.ButterKnife;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subscribers.DisposableSubscriber;
import static android.text.TextUtils.isEmpty;
import static android.util.Patterns.EMAIL_ADDRESS;

public class NewCashDocFragment extends Fragment {

    public NewCashDocFragment() {

    }

    TextView _btnValidIndicator;
    @Bind(R.id.datex) EditText _datex;
    @Bind(R.id.companyid) EditText _companyid;
    @Bind(R.id.person) EditText _person;
    @Bind(R.id.memo) EditText _memo;
    @Bind(R.id.hod) EditText _hod;


    private DisposableSubscriber<Boolean> _disposableObserver = null;
    private Flowable<CharSequence> _hodChangeObservable;
    private Flowable<CharSequence> _personChangeObservable;
    private Flowable<CharSequence> _memoChangeObservable;
    private Flowable<Boolean> _icoChangeObservable;
    Observable<String> obsIco;

    private ProgressBar mProgressBar;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    DgAllEmpsAbsMvvmViewModel mViewModel;

    @Inject
    RxBus _rxBus;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SamfantozziApp) getActivity().getApplication()).dgaeacomponent().inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_newcashdoc, container, false);
        ButterKnife.bind(this, layout);

        //mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        _personChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_person)
                .skip(1));
        _memoChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_memo)
                .skip(1));
        _hodChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_hod)
                .skip(1));


        obsIco = RxJavaInterop.toV2Observable(RxTextView
                .textChanges(_companyid)
                .filter(charSequence -> charSequence.length() > 3)
                .debounce(300, TimeUnit.MILLISECONDS)).map(charSequence -> charSequence.toString());

        obsIco.subscribe(string -> {
            Log.d("NewCashDoc", "debounced " + string);
            mViewModel.emitMyObservableIdCompany(string);
        });

        _icoChangeObservable = RxJavaInterop.toV2Flowable(mViewModel.getMyObservableIdCompany());

        _combineLatestEvents();

        return layout;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        _disposableObserver.dispose();
       obsIco.subscribe();
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

        ActivityCompat.invalidateOptionsMenu(getActivity());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mSharedPreferences.getString("pokluce", "")
                + " " +  getString(R.string.newdoc));
 }

    private void unBind() {

        //hideProgressBar();

    }


    public static class ClickFobEvent {}

    protected void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void _combineLatestEvents() {

        _disposableObserver = new DisposableSubscriber<Boolean>() {
            @Override
            public void onNext(Boolean formValid) {
                if (formValid) {
                    _btnValidIndicator.setBackgroundColor(getResources().getColor(R.color.blue));
                }
                else {
                    _btnValidIndicator.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("", "there was an error ");
            }

            @Override
            public void onComplete() {
                Log.d("","completed");
            }
        };

        Flowable
                .combineLatest(_personChangeObservable,
                        _memoChangeObservable,
                        _hodChangeObservable,
                        _icoChangeObservable,
                        (newPerson, newMemo, newHod, newIco) -> {

                            if (!newIco) {
                                _companyid.setError("Company ID does not match!");
                            }


                            boolean personValid = !isEmpty(newPerson) && newPerson.length() > 8;
                            if (!personValid) {
                                _person.setError("Invalid Person!");
                            }

                            boolean memoValid = !isEmpty(newMemo) && newMemo.length() > 8;
                            if (!memoValid) {
                                _memo.setError("Invalid Memo!");
                            }

                            boolean hodValid = !isEmpty(newHod);
                            if (hodValid) {
                                int num = Integer.parseInt(newHod.toString());
                                hodValid = num > 0 && num <= 100;
                            }
                            if (!hodValid) {
                                _hod.setError("Invalid Hod!");
                            }

                            return personValid && memoValid && hodValid && newIco;
                        })
                .subscribe(_disposableObserver);
    }


}
