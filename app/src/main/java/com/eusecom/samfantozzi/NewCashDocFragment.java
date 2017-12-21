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

    @Bind(R.id.btn_demo_form_valid)
    TextView _btnValidIndicator;
    @Bind(R.id.demo_combl_email) EditText _email;
    @Bind(R.id.demo_combl_password) EditText _password;
    @Bind(R.id.demo_combl_num) EditText _number;

    @Bind(R.id.datex) EditText _datex;
    @Bind(R.id.companyid) EditText _companyid;


    private DisposableSubscriber<Boolean> _disposableObserver = null;
    private Flowable<CharSequence> _emailChangeObservable;
    private Flowable<CharSequence> _numberChangeObservable;
    private Flowable<CharSequence> _passwordChangeObservable;
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

        _emailChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_email)
                .skip(1));
        _passwordChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_password)
                .skip(1));
        _numberChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_number)
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
                .combineLatest(_emailChangeObservable,
                        _passwordChangeObservable,
                        _numberChangeObservable,
                        _icoChangeObservable,
                        (newEmail, newPassword, newNumber, newIco) -> {

                            if (!newIco) {
                                _companyid.setError("Company ID does not match!");
                            }

                            boolean emailValid = !isEmpty(newEmail) &&
                                    EMAIL_ADDRESS
                                            .matcher(newEmail)
                                            .matches();
                            if (!emailValid) {
                                _email.setError("Invalid Email!");
                            }

                            boolean passValid = !isEmpty(newPassword) && newPassword.length() > 8;
                            if (!passValid) {
                                _password.setError("Invalid Password!");
                            }

                            boolean numValid = !isEmpty(newNumber);
                            if (numValid) {
                                int num = Integer.parseInt(newNumber.toString());
                                numValid = num > 0 && num <= 100;
                            }
                            if (!numValid) {
                                _number.setError("Invalid Number!");
                            }

                            return emailValid && passValid && numValid && newIco;
                        })
                .subscribe(_disposableObserver);
    }


}
