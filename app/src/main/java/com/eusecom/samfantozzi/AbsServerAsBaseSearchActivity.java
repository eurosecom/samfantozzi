/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.eusecom.samfantozzi;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.retrofit.AbsServerClient;
import com.eusecom.samfantozzi.retrofit.RfEtestApi;
import com.eusecom.samfantozzi.retrofit.RfEtestService;
import com.eusecom.samfantozzi.rxbus.RxBus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Subscription;

public abstract class AbsServerAsBaseSearchActivity extends AppCompatActivity {

  protected AbsServerSearchEngine mAbsServerSearchEngine;
  protected EditText mQueryEditText;
  protected Button mSearchButton;
  private AbsServerAsAdapter mAdapter;
  private ProgressBar mProgressBar;
  Toolbar mActionBarToolbar;
  private RxBus _rxBus;
  private CompositeDisposable _disposables;

  private RfEtestApi _githubService;
  private Subscription subscription;

  private View.OnClickListener onclicklistapprove = null;
  private View.OnClickListener onclicklistrefuse = null;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_absserver);

    //mActionBarToolbar = (Toolbar) findViewById(R.id.tool_bar);
    //setSupportActionBar(mActionBarToolbar);
    getSupportActionBar().setTitle(getString(R.string.action_absmysql));

    mQueryEditText = (EditText) findViewById(R.id.query_edit_text);
    mSearchButton = (Button) findViewById(R.id.search_button);
    mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

    String githubToken = Constants.ETEST_API_KEY;
    String urlx = "http:\\" + SettingsActivity.getServerName(this);
    String usicox = SettingsActivity.getUsIco(this);
    if( usicox.equals("44551142")) {
      urlx = "http:\\" + Constants.EDCOM_url;
    }
    _githubService = RfEtestService.createGithubService(githubToken, urlx);

    //cheeses = Arrays.asList(getResources().getStringArray(R.array.cheeses3));

    _rxBus = getRxBusSingleton();

    RecyclerView list = (RecyclerView) findViewById(R.id.list);
    list.setLayoutManager(new LinearLayoutManager(this));
    list.setAdapter(mAdapter = new AbsServerAsAdapter(_rxBus));

    _disposables = new CompositeDisposable();

    ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();

    _disposables
            .add(tapEventEmitter.subscribe(event -> {
              if (event instanceof AbsServerAsBaseSearchActivity.TapEvent) {
                ///_showTapText();
              }
              if (event instanceof Attendance) {
                ///tvContent = (TextView) findViewById(R.id.tvContent);
                ///tvContent.setText(((EventRxBus.Message) event).message); OK change event instanceof to EventRxBus.Message
                ///_showTapTextToast(((EventRxBus.Absence) event).daod + " / " + ((EventRxBus.Absence) event).dado); OK change event instanceof to EventRxBus.Absence
                saveAbsServer(((Attendance) event).daod + " / " + ((Attendance) event).dado, ((Attendance) event));
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

  protected void showProgressBar() {
    mProgressBar.setVisibility(View.VISIBLE);
  }

  protected void hideProgressBar() {
    mProgressBar.setVisibility(View.GONE);
  }


  protected void showResultAs(List<Attendance> resultAs) {

    if (resultAs.isEmpty()) {
      Toast.makeText(this, R.string.nothing_found, Toast.LENGTH_SHORT).show();
      mAdapter.setAbsserver(Collections.<Attendance>emptyList());
    } else {
      //Log.d("showResultAs ", resultAs.get(0).dmna);
      mAdapter.setAbsserver(resultAs);
    }
  }

  protected void nastavResultAs(List<Attendance> resultAs) {
      mAbsServerSearchEngine = new AbsServerSearchEngine(resultAs);
  }


  public RxBus getRxBusSingleton() {
    if (_rxBus == null) {
      _rxBus = new RxBus();
    }

    return _rxBus;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    Log.d("ondestroy ", "absserverbasesearchactivity");
    _disposables.clear();
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }

  }


  private void saveAbsServer(String texttoast, Attendance model) {

    Toast.makeText(AbsServerAsBaseSearchActivity.this, texttoast,
            Toast.LENGTH_LONG).show();
    //Log.d("_showTapTextToast", model.datm);

    // [START declare_database_ref]
    DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference();
    // [END declare_database_ref]

    String userIDX = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String key = mDatabase.child("absences").push().getKey();
    String gpslat="0";
    String gpslon="0";
    String icox = SettingsActivity.getUsIco(AbsServerAsBaseSearchActivity.this);

    SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat fff = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    long daodl = 0l; long dadol = 0l; long datml = 0l;
    try {
      Date d = f.parse(model.daod);
      daodl = d.getTime() / 1000;
      Date dd = f.parse(model.dado);
      dadol = dd.getTime() / 1000;
      Date ddd = fff.parse(model.datm);
      datml = ddd.getTime() / 1000;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    String daods = daodl + "";
    String dados = dadol + "";
    String datms = datml + "";
    //Log.d("_showTapTextToast", datms);
    String cplxb = model.longi;

    writeAbsenceServerToFB(icox, userIDX, model.ume, model.dmxa, model.dmna, daods, dados, model.dnixa,
            model.hodxb, gpslon, gpslat, datms, model.usosc,
            mDatabase, key, cplxb, model.usname );

    writeKeyfToServer(icox, userIDX, model.ume, model.dmxa, model.dmna, daods, dados, model.dnixa,
            model.hodxb, gpslon, gpslat, datms, model.usosc,
            mDatabase, key, cplxb );

  }//end saveAbsServer

  private void writeKeyfToServer(String usico, String usid, String ume, String dmxa, String dmna, String daod, String dado, String dnixa,
                                      String hodxb, String longi, String lati, String datm, String usosc,
                                      DatabaseReference mDatabase, String keyf, String cplxb ) {

    String getfromfir =  SettingsActivity.getFir(AbsServerAsBaseSearchActivity.this);
    String usicox = SettingsActivity.getUsIco(AbsServerAsBaseSearchActivity.this);
    if( usicox.equals("44551142")) {
      getfromfir  = Constants.EDCOM_fir;
    }
    String urlx = "http:\\" + SettingsActivity.getServerName(this);
    if( usicox.equals("44551142")) {
      urlx = "http:\\" + Constants.EDCOM_url;
    }
    subscription = AbsServerClient.getInstance(urlx)
            .setKeyAndgetAbsServer(getfromfir, keyf, cplxb)
            .subscribeOn(rx.schedulers.Schedulers.io())
            .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(new rx.Observer<List<Attendance>>() {
              @Override public void onCompleted() {
                hideProgressBar();
                //Log.d("", "setKEYF onCompleted()");
              }

              @Override public void onError(Throwable e) {
                e.printStackTrace();
                hideProgressBar();
                //Log.d("", "In onError()");
              }

              @Override public void onNext(List<Attendance> absserverRepos) {
                //Log.d("Thread onNext", Thread.currentThread().getName());
                //Log.d("setKEYF onNext", absserverRepos.get(0).getDmna());
                nastavResultAs(absserverRepos);
                showResultAs(absserverRepos);
                //getDialogWhatNext();

              }
            });

  }


  private void writeAbsenceServerToFB(String usico, String usid, String ume, String dmxa, String dmna, String daod, String dado, String dnixa,
                            String hodxb, String longi, String lati, String datm, String usosc,
                                  DatabaseReference mDatabase, String key, String cplxb, String usname ) {


    Attendance attendance = new Attendance(usico, usid, ume, dmxa, dmna, daod, dado, dnixa, hodxb, longi, lati, datm, usosc, usname );
    attendance.setCplxb(cplxb);

    Map<String, Object> attValues = attendance.toMap();

    Map<String, Object> childUpdates = new HashMap<>();

    childUpdates.put("/absences/" + key, attValues);
    childUpdates.put("/company-absences/" + usico + "/" + key, attValues);
    childUpdates.put("/user-absences/" + usid + "/" + key, attValues);

    mDatabase.updateChildren(childUpdates);


  }//END writeAbsenceServer


  public static class TapEvent {}

}
