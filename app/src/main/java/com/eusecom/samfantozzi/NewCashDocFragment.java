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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.eusecom.samfantozzi.rxbus.RxBus;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    @Bind(R.id.invoice) EditText _invoice;
    @Bind(R.id.memo) EditText _memo;
    @Bind(R.id.hod) EditText _hod;
    Button datebutton, idbutton, _btnsave;
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
    @Bind(R.id.inputPoh) EditText _inputPoh;

    @NonNull
    private CompositeSubscription mSubscription;
    private CompositeDisposable _disposables;

    private DisposableSubscriber<Boolean> _disposableObserver = null;
    private Flowable<CharSequence> _datexChangeObservable;
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
    Spinner spinner;
    protected ArrayAdapter<Account> mAdapter;

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

        _btnsave = (Button) layout.findViewById(R.id.btnsave);
        _btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("NewCashDoc", "Clicked save ");
                Toast.makeText(getActivity(), "Clicked save", Toast.LENGTH_SHORT).show();

            }
        });


        spinner = (Spinner) layout.findViewById(R.id.spinnerPoh);

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
        _disposables.add(RxJavaInterop.toV2Observable(RxTextView
                .textChanges(_inputZk0)
                .filter(charSequence -> charSequence.length() > 0)
                .debounce(600, TimeUnit.MILLISECONDS)).map(charSequence -> charSequence.toString())
                .subscribe(string -> {
                    Log.d("NewCashLog frg2", "debounced " + string);
                    CalcVatKt calcvatx = getCalcVat(0);
                    mViewModel.emitMyObservableRecount(calcvatx);
                })
        );
        _disposables.add(RxJavaInterop.toV2Observable(RxTextView
                .textChanges(_inputZk2)
                .filter(charSequence -> charSequence.length() > 0)
                .debounce(600, TimeUnit.MILLISECONDS)).map(charSequence -> charSequence.toString())
                .subscribe(string -> {
                    Log.d("NewCashLog frg2", "debounced " + string);
                    CalcVatKt calcvatx = getCalcVat(2);
                    mViewModel.emitMyObservableRecount(calcvatx);
                })
        );
        _disposables.add(RxJavaInterop.toV2Observable(RxTextView
                .textChanges(_inputDn2)
                .filter(charSequence -> charSequence.length() > 0)
                .debounce(600, TimeUnit.MILLISECONDS)).map(charSequence -> charSequence.toString())
                .subscribe(string -> {
                    Log.d("NewCashLog frg2", "debounced " + string);
                    CalcVatKt calcvatx = getCalcVat(22);
                    mViewModel.emitMyObservableRecount(calcvatx);
                })
        );
        _disposables.add(RxJavaInterop.toV2Observable(RxTextView
                .textChanges(_inputZk1)
                .filter(charSequence -> charSequence.length() > 0)
                .debounce(600, TimeUnit.MILLISECONDS)).map(charSequence -> charSequence.toString())
                .subscribe(string -> {
                    Log.d("NewCashLog frg2", "debounced " + string);
                    CalcVatKt calcvatx = getCalcVat(1);
                    mViewModel.emitMyObservableRecount(calcvatx);
                })
        );
        _disposables.add(RxJavaInterop.toV2Observable(RxTextView
                .textChanges(_inputDn1)
                .filter(charSequence -> charSequence.length() > 0)
                .debounce(600, TimeUnit.MILLISECONDS)).map(charSequence -> charSequence.toString())
                .subscribe(string -> {
                    Log.d("NewCashLog frg2", "debounced " + string);
                    CalcVatKt calcvatx = getCalcVat(11);
                    mViewModel.emitMyObservableRecount(calcvatx);
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
                        //Toast.makeText(getActivity(), icox, Toast.LENGTH_SHORT).show();
                        //_idcexist.setText("true");
                        //_companyid.setError(null);
                        _companyid.setText(icox);



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
                _companyid.setError(null);
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
        mViewModel.clearObservableRecount();

        Log.d("NewCashLog ", "onDestroy ");

    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
        mSubscription = new CompositeSubscription();

        mSubscription.add(mViewModel.getMyPohybyFromSqlServer("3", drupoh)
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error NewCashDocFragment " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(getActivity(), "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setPohyby));

        mSubscription.add(mViewModel.getMyObservableIdModelCompany()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error NewCashDocFragment " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(getActivity(), "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setIdCompanyKt));

        mSubscription.add(mViewModel.getMyObservableRecount()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error NewCashDocFragment " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(getActivity(), "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setRecount));

    }

    @Override
    public void onPause() {
        super.onPause();
        unBind();
        mSubscription.clear();
    }

    private void setRecount(@NonNull final CalcVatKt result) {

        //String serverx = result.getNod() + "";
        //Toast.makeText(getActivity(), "Recalculated nod " + serverx, Toast.LENGTH_SHORT).show();
        //if( result.getWinp() != 0 ) { _inputZk0.setText(result.getZk0() + ""); }
        //if( result.getWinp() != 1 ) { _inputZk1.setText(result.getZk1() + ""); }
        //if( result.getWinp() != 2 ) { _inputZk2.setText(result.getZk2() + ""); }
        if( result.getWinp() == 1 ) { _inputDn1.setText(result.getDn1() + ""); }
        if( result.getWinp() == 2 ) { _inputDn2.setText(result.getDn2() + ""); }
        _hod.setText(result.getNod() + "");

    }

    private void setPohyby(@NonNull final List<Account> pohyby) {

        if (pohyby.size() > 0) {
            //String serverx = pohyby.get(0).getAccname();
            //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();

            ArrayList<Account> pohybys = new ArrayList<>();

            for (int i = 0; i < pohyby.size(); i++) {
                pohybys.add(new Account(pohyby.get(i).getAccname(), pohyby.get(i).getAccnumber(), "", "", "", true));
            }

            mAdapter = new ArrayAdapter<Account>(getActivity(), android.R.layout.simple_spinner_item, pohybys);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(mAdapter);
            if(_inputPoh.getText().toString().equals("")) { _inputPoh.setText(pohyby.get(0).getAccnumber()); }
            if(_inputPoh.getText().toString().equals("0")) { _inputPoh.setText(pohyby.get(0).getAccnumber()); }
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    // Here you get the current item (a User object) that is selected by its position
                    Account account = mAdapter.getItem(position);
                    _inputPoh.setText(account.getAccnumber());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });

        }

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

        if( drupoh.equals("1") ) {
            spinner.setPrompt(getString(R.string.select_receipt));
        }else{
            spinner.setPrompt(getString(R.string.select_expense));
        }

        //if(_inputZk0.getText().toString().equals("")) { _inputZk0.setText("0"); }
        //if(_inputZk1.getText().toString().equals("")) { _inputZk1.setText("0"); }
        //if(_inputZk2.getText().toString().equals("")) { _inputZk2.setText("0"); }
        //if(_inputDn1.getText().toString().equals("")) { _inputDn1.setText("0"); }
        //if(_inputDn2.getText().toString().equals("")) { _inputDn2.setText("0"); }
        //if(_hod.getText().toString().equals("")) { _hod.setText("0"); }
        if(_inputPoh.getText().toString().equals("")) { _inputPoh.setText("0"); }

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
                    //_btnsave.setBackgroundColor(getResources().getColor(R.color.material_light_blue_A200));
                    _btnsave.setEnabled(true);
                    _btnsave.setClickable(true);
                    Log.d("NewCashDoc", "formvalid true ");
                }
                else {
                    //_btnsave.setBackgroundColor(getResources().getColor(R.color.gray));
                    _btnsave.setEnabled(false);
                    _btnsave.setClickable(false);
                    Log.d("NewCashDoc", "formvalid false ");
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
                        _datexChangeObservable,
                        _icoChangeObservable,
                        (newPerson, newMemo, newDatex, newIco) -> {

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


                            boolean icoxValid = newIco.toString().equals("true");

                            return personValid && memoValid && datexValid && icoxValid;
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

    private CalcVatKt getCalcVat(int winp) {

        String zk0s = _inputZk0.getText().toString();
        String zk1s = _inputZk1.getText().toString();
        String zk2s = _inputZk2.getText().toString();
        String dn1s = _inputDn1.getText().toString();
        String dn2s = _inputDn2.getText().toString();
        String hods = _hod.getText().toString();

        if(zk0s.equals("")){ zk0s="0"; }
        if(zk1s.equals("")){ zk1s="0"; }
        if(zk2s.equals("")){ zk2s="0"; }
        if(dn1s.equals("")){ dn1s="0"; }
        if(dn2s.equals("")){ dn2s="0"; }
        if(hods.equals("")){ hods="0"; }

        if(zk0s.equals(".")){ zk0s="0"; }
        if(zk1s.equals(".")){ zk1s="0"; }
        if(zk2s.equals(".")){ zk2s="0"; }
        if(dn1s.equals(".")){ dn1s="0"; }
        if(dn2s.equals(".")){ dn2s="0"; }
        if(hods.equals(".")){ hods="0"; }

        if(zk0s.equals("-")){ zk0s="0"; }
        if(zk1s.equals("-")){ zk1s="0"; }
        if(zk2s.equals("-")){ zk2s="0"; }
        if(dn1s.equals("-")){ dn1s="0"; }
        if(dn2s.equals("-")){ dn2s="0"; }
        if(hods.equals("-")){ hods="0"; }

        if(zk0s.equals("+")){ zk0s="0"; }
        if(zk1s.equals("+")){ zk1s="0"; }
        if(zk2s.equals("+")){ zk2s="0"; }
        if(dn1s.equals("+")){ dn1s="0"; }
        if(dn2s.equals("+")){ dn2s="0"; }
        if(hods.equals("+")){ hods="0"; }

        Double zk0d = Double.valueOf(zk0s);
        Double zk1d = Double.valueOf(zk1s);
        Double zk2d = Double.valueOf(zk2s);
        Double dn1d = Double.valueOf(dn1s);
        Double dn2d = Double.valueOf(dn2s);
        Double hodd = Double.valueOf(hods);

        CalcVatKt newcalcvat = new CalcVatKt(zk0d, zk1d, zk2d, dn1d, dn2d, hodd, 0d, winp,true);

        return newcalcvat;
    }


}
