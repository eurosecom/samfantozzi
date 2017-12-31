package com.eusecom.samfantozzi;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.eusecom.samfantozzi.rxbus.RxBus;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import butterknife.Bind;
import butterknife.ButterKnife;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.subscribers.DisposableSubscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;
import static rx.Observable.empty;


public class NewCashDocFragment extends Fragment {

    public NewCashDocFragment() {

    }

    @Bind(R.id.datex) EditText _datex;
    @Bind(R.id.companyid) EditText _companyid;
    @Bind(R.id.idcexist) EditText _idcexist;
    @Bind(R.id.person) EditText _person;
    @Bind(R.id.memo) EditText _memo;
    @Bind(R.id.hod) EditText _hod;
    @Bind(R.id.btnsave) Button _btnsave;
    Button datebutton, idbutton;
    @Bind(R.id.textzakl2) TextView _textzakl2;
    @Bind(R.id.textdph2) TextView _textdph2;
    @Bind(R.id.textzakl1) TextView _textzakl1;
    @Bind(R.id.textdph1) TextView _textdph1;
    @Bind(R.id.companyname) TextView _companyname;
    @Bind(R.id.inputZk0) EditText _inputZk0;
    @Bind(R.id.inputZk1) EditText _inputZk1;
    @Bind(R.id.inputZk2) EditText _inputZk2;
    @Bind(R.id.inputDn1) EditText _inputDn1;
    @Bind(R.id.inputDn2) EditText _inputDn2;

    @NonNull
    private CompositeSubscription mSubscription;
    private CompositeDisposable _disposables;

    private DisposableSubscriber<Boolean> _disposableObserver = null;
    private Flowable<CharSequence> _datexChangeObservable;
    private Flowable<CharSequence> _hodChangeObservable;
    private Flowable<CharSequence> _personChangeObservable;
    private Flowable<CharSequence> _memoChangeObservable;
    private Flowable<CharSequence> _icoChangeObservable;

    private ProgressBar mProgressBar;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    DgAllEmpsAbsMvvmViewModel mViewModel;

    @Inject
    RxBus _rxBus;

    String drupoh = "1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SamfantozziApp) getActivity().getApplication()).dgaeacomponent().inject(this);
        drupoh = mSharedPreferences.getString("drupoh", "1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_newcashdoc, container, false);
        ButterKnife.bind(this, layout);

        datebutton = (Button) layout.findViewById(R.id.datebutton);
        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker(_datex.getText().toString()).show();
                //mViewModel.getDatePickerFromMvvm(_datex.getText().toString(), getString(R.string.datedialogpos), getActivity()).show();


            }
        });

        idbutton = (Button) layout.findViewById(R.id.idbutton);
        idbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(),TypesKtActivity.class);
                //Intent in = new Intent(getActivity(),ChooseMonthActivity.class);
                Bundle extras = new Bundle();
                extras.putString("fromact", "3");
                in.putExtras(extras);
                startActivityForResult(in, 201);
                //startActivity(in );

            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _datexChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_datex)
                .skip(1));
        _personChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_person)
                .skip(1));
        _memoChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_memo)
                .skip(1));
        _hodChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_hod)
                .skip(1));

        _icoChangeObservable = RxJavaInterop.toV2Flowable(RxTextView
                .textChanges(_idcexist)
                .skip(1));


        _disposables = new CompositeDisposable();
        _disposables.add(RxJavaInterop.toV2Observable(RxTextView
                        .textChanges(_companyid)
                        .filter(charSequence -> charSequence.length() > 1)
                        .debounce(600, TimeUnit.MILLISECONDS)).map(charSequence -> charSequence.toString())
                        .subscribe(string -> {
                            Log.d("NewCashLog frg2", "debounced " + string);
                            //mViewModel.emitMyObservableIdCompany(string);
                            mViewModel.emitMyObservableIdModelCompany(string);
                        })
         );

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();
        _disposables
                .add(tapEventEmitter.subscribe(event -> {

                    Log.d("NewCashLog frg2", "tapEventEmitter ");

                    if (event instanceof SupplierListFragment.ClickFobEvent) {
                        //Log.d("SupplierListFragment  ", " fobClick ");
                        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();

                    }
                    if (event instanceof IdcChoosenKt) {
                        String icox = ((IdcChoosenKt) event).getIco();
                        Toast.makeText(getActivity(), icox, Toast.LENGTH_SHORT).show();
                        //_companyid.setText(icox);


                    }

                }));
        _disposables.add(tapEventEmitter.connect());

        _combineLatestEvents();

        Log.d("NewCashLog ", "onActivityCreated ");
    }

    protected void setIdCompanyKt(List<IdCompanyKt> resultAs) {

        if (resultAs.size() > 0) {
            Log.d("NewCashLog Idc0 ", resultAs.get(0).getNai());
            Boolean icoValid = resultAs.get(0).getLogprx();
            //_disposableObserver.onNext(icoValid);
            if (!icoValid) {
                _companyid.setError("Company ID " + resultAs.get(0).getIco() + " does not match!");
                _companyname.setText(resultAs.get(0).getNai());
                _idcexist.setText("false");
            } else {
                _companyname.setText(resultAs.get(0).getNai());
                _idcexist.setText("true");
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        _disposableObserver.dispose();
        mSubscription.clear();
        _disposables.dispose();
        mViewModel.clearObservableIdModelCompany();

        Log.d("NewCashLog ", "onDestroy ");

    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
        mSubscription = new CompositeSubscription();
        mSubscription.add(mViewModel.getMyObservableIdModelCompany()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error NewCashDocFragment " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(getActivity(), "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setIdCompanyKt));
    }

    @Override
    public void onPause() {
        super.onPause();
        unBind();
        mSubscription.clear();
    }


    private void bind() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c.getTime());
        _datex.setText(formattedDate);
        _datex.setEnabled(false);
        _textzakl2.setText(String.format(getResources().getString(R.string.popzakl2), mSharedPreferences.getString("firdph2", "")) + "%");
        _textdph2.setText(String.format(getResources().getString(R.string.popdph2), mSharedPreferences.getString("firdph2", "")) + "%");
        _textzakl1.setText(String.format(getResources().getString(R.string.popzakl1), mSharedPreferences.getString("firdph1", "")) + "%");
        _textdph1.setText(String.format(getResources().getString(R.string.popdph1), mSharedPreferences.getString("firdph1", "")) + "%");
        ActivityCompat.invalidateOptionsMenu(getActivity());

        if(drupoh.equals("1")) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mSharedPreferences.getString("pokluce", "")
                    + " " +  getString(R.string.newreceipt));
        }else{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mSharedPreferences.getString("pokluce", "")
                    + " " +  getString(R.string.newexpense));
        }

        if(_inputZk0.getText().toString().equals("")) { _inputZk0.setText("0"); }
        if(_inputZk1.getText().toString().equals("")) { _inputZk1.setText("0"); }
        if(_inputZk2.getText().toString().equals("")) { _inputZk2.setText("0"); }
        if(_inputDn1.getText().toString().equals("")) { _inputDn1.setText("0"); }
        if(_inputDn2.getText().toString().equals("")) { _inputDn2.setText("0"); }
        if(_hod.getText().toString().equals("")) { _hod.setText("0"); }

    }

    private void unBind() {


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
                    _btnsave.setBackgroundColor(getResources().getColor(R.color.blue));
                    //Log.d("NewCashDoc", "formvalid true ");
                }
                else {
                    _btnsave.setBackgroundColor(getResources().getColor(R.color.gray));
                    //Log.d("NewCashDoc", "formvalid false ");
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
                        _datexChangeObservable,
                        _icoChangeObservable,
                        (newPerson, newMemo, newHod, newDatex, newIco) -> {

                            boolean datexValid = !isEmpty(newDatex);
                            if (!datexValid) {
                                _datex.setError("Invalid Date!");
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

                            boolean icoxValid = newIco.toString().equals("true");

                            return personValid && memoValid && hodValid && datexValid && icoxValid;
                        })
                .subscribe(_disposableObserver);
    }

    private DatePickerDialog getDatePicker(String datumx) {

        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        //dd = 38;

        //String datumx = "12.12.2017";

        String delims = "[.]+";
        String[] datumxxx = datumx.split(delims);

        String ddx = datumxxx[0];
        String mmx = datumxxx[1];
        String yyx = datumxxx[2];

        int ddi = Integer.parseInt(ddx);
        int mmi = Integer.parseInt(mmx);
        int yyi = Integer.parseInt(yyx);
        dd=ddi; mm=mmi-1; yy=yyi;

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), null, yy, mm, dd);
        //dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Button Neg Text", dpd);
        dpd.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.datedialogpos), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {

                    int dayx = dpd.getDatePicker().getDayOfMonth();
                    int monthx = dpd.getDatePicker().getMonth();
                    int yearx = dpd.getDatePicker().getYear();
                    int monthy = monthx + 1;

                    Log.d("NewCashLog dayx ", dayx + "");
                    _datex.setText(dayx + "." + monthy + "." + yearx);
                }
            }

        });


        return dpd;
    }


}
