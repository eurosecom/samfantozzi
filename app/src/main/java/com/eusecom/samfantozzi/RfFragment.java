package com.eusecom.samfantozzi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.eusecom.samfantozzi.soaphello.HelloApi;
import com.eusecom.samfantozzi.soaphello.HelloRequestBody;
import com.eusecom.samfantozzi.soaphello.HelloRequestEnvelope;
import com.eusecom.samfantozzi.soaphello.HelloRequestModel;
import com.eusecom.samfantozzi.soaphello.HelloResponseEnvelope;
import com.eusecom.samfantozzi.soaphello.HelloService;
import com.eusecom.samfantozzi.soapweather.RequestBody;
import com.eusecom.samfantozzi.soapweather.RequestEnvelope;
import com.eusecom.samfantozzi.soapweather.RequestModel;
import com.eusecom.samfantozzi.soapweather.ResponseEnvelope;
import com.eusecom.samfantozzi.soapweather.WeatherApi;
import com.eusecom.samfantozzi.soapweather.WeatherService;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import static android.text.TextUtils.isEmpty;
import static java.lang.String.format;

public class RfFragment extends Fragment {

    @BindView(R.id.demo_retrofit_contributors_username) EditText _username;
    @BindView(R.id.demo_retrofit_contributors_repository) EditText _repo;
    @BindView(R.id.log_list) ListView _resultList;

    private ArrayAdapter<String> _adapter;
    private RfGithubApi _githubService;
    private WeatherApi _weatherService;
    private HelloApi _helloService;
    private CompositeDisposable _disposables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String githubToken = Constants.GITHUB_API_KEY;
        String githubToken2 = Constants.GITHUB_API_KEY2;
        _githubService = RfGithubService.createGithubService(githubToken, githubToken2);
        _weatherService = WeatherService.createWeatherService(githubToken, githubToken2);
        _helloService = HelloService.createHelloService(githubToken, githubToken2);
        _disposables = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.rffragment_retrofit, container, false);
        ButterKnife.bind(this, layout);

        _adapter = new ArrayAdapter<>(getActivity(), R.layout.rfitem_retrofitlog, R.id.item_log, new ArrayList<>());
        _resultList.setAdapter(_adapter);

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _disposables.dispose();
    }

    @OnClick(R.id.bt_make_request)
    public void onMakeRequestClicked() {
        Log.d("RfFragment", "on MakeRequest Clicked");
        hideKeyboard();

        RequestEnvelope requestEnvelop = new RequestEnvelope();
        RequestBody requestBody = new RequestBody();
        RequestModel requestModel = new RequestModel();
        requestModel.theCityName = "vienna";
        requestModel.cityNameAttribute = "http://WebXml.com.cn/";
        requestBody.getWeatherbyCityName = requestModel;
        requestEnvelop.body = requestBody;

        _adapter.clear();

        _disposables.add(//
                _weatherService.getWeatherbyCityName(requestEnvelop)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<ResponseEnvelope>() {

                            @Override
                            public void onComplete() {
                                Log.d("Rf Weather completed", "");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e( "Rf Weather error ", e.toString());
                            }

                            @Override
                            public void onNext(ResponseEnvelope contributors) {
                                Log.d("Rf Weather onnext", "");
                            }
                        }));

    }

    @OnClick(R.id.bt_make_request2)
    public void onHelloClicked() {
        Log.d("RfFragment", "on Hello Clicked");
        hideKeyboard();

        HelloRequestEnvelope requestEnvelop = new HelloRequestEnvelope();
        HelloRequestBody requestBody = new HelloRequestBody();
        HelloRequestModel requestModel = new HelloRequestModel();
        //requestModel.theCityName = "vienna";
        requestModel.setHelloAttribute = "http://Wsdl2CodeTestService/";
        requestBody.getHelloString = requestModel;
        requestEnvelop.body = requestBody;

        _adapter.clear();

        _disposables.add(//
                _helloService.getWeatherbyCityName(requestEnvelop)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<HelloResponseEnvelope>() {

                            @Override
                            public void onComplete() {
                                Log.d("Rf Hello completed", "");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e( "Rf Hello error ", e.toString());
                            }

                            @Override
                            public void onNext(HelloResponseEnvelope responseEnvelop) {
                                Log.d("Rf Hello onnext", "");

                                if (responseEnvelop != null ) {
                                    String helloresult = responseEnvelop.body.getHelloResponse.result;
                                    Log.d("Rf Hello result", helloresult);
                                    Toast.makeText(getActivity(), helloresult, Toast.LENGTH_LONG).show();
                                }

                            }
                        }));

    }

    @OnClick(R.id.btn_demo_retrofit_contributors)
    public void onListContributorsClicked() {
        _adapter.clear();

        _disposables.add(//
              _githubService.contributors(_username.getText().toString(), _repo.getText().toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<List<RfContributor>>() {

                        @Override
                        public void onComplete() {
                            Log.d("Retrof call 1 completed", "");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e( "woops we got an error ", e.toString());
                        }

                        @Override
                        public void onNext(List<RfContributor> contributors) {
                            for (RfContributor c : contributors) {
                                _adapter.add(format("%s has made %d contributions to %s",
                                      c.login,
                                      c.contributions,
                                      _repo.getText().toString()));

                            }
                        }
                    }));
    }

    @OnClick(R.id.btn_demo_retrofit_contributors_with_user_info)
    public void onListContributorsWithFullUserInfoClicked() {
        _adapter.clear();

        _disposables.add(_githubService.contributors(_username.getText().toString(), _repo.getText().toString())
              .flatMap(Observable::fromIterable)
              .flatMap(contributor -> {
                  Observable<RfUser> _userObservable = _githubService.user(contributor.login)
                        .filter(user -> !isEmpty(user.name) && !isEmpty(user.email));

                  return Observable.zip(_userObservable,
                        Observable.just(contributor),
                        Pair::new);
              })
              .subscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeWith(new DisposableObserver<Pair<RfUser,RfContributor>>() {
                  @Override
                  public void onComplete() {
                      //Timber.d("Retrofit call 2 completed ");
                  }

                  @Override
                  public void onError(Throwable e) {
                      //Timber.e(e, "error while getting the list of contributors along with full " + "names");
                  }

                  @Override
                  public void onNext(Pair pair) {
                      RfUser user = ((Pair<RfUser, RfContributor>)pair).first;
                      RfContributor contributor = ((Pair<RfUser, RfContributor>)pair).second;

                      _adapter.add(format("%s(%s) has made %d contributions to %s",
                            user.name,
                            user.email,
                            contributor.contributions,
                            _repo.getText().toString()));

                      _adapter.notifyDataSetChanged();

                  }
              }));
    }

    private void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

}
