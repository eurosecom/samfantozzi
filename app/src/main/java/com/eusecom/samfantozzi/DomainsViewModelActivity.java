package com.eusecom.samfantozzi;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.samfantozzi.realm.RealmDomain;
import com.eusecom.samfantozzi.realm.RealmInvoice;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

public class DomainsViewModelActivity extends AppCompatActivity {

    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    DgAllEmpsAbsIDataModel mDgAllEmpsAbsIDataModel;

    @BindView(R.id.click_count_text)
    protected TextView clickCountText;

    @BindView(R.id.recyclerview)
    protected RecyclerView mRecyclerView;

    private DomainsViewModel viewModel;

    private CompositeSubscription mSubscription;

    private DomainsAdapter mAdapter;
    private DomainsAdapter.ClickOnItemListener listener;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SamfantozziApp) getApplication()).dgaeacomponent().inject(this);

        setContentView(R.layout.domains_activity);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // the factory and its dependencies instead should be injected with DI framework like Dagger
        DomainsViewModelFactory factory =
                new DomainsViewModelFactory(new LoggingClickInterceptor(), mDgAllEmpsAbsIDataModel);

        viewModel = ViewModelProviders.of(this, factory).get(DomainsViewModel.class);
        displayClickCount(viewModel.getCount());

        getSupportActionBar().setTitle(getString(R.string.action_setdomain) + " / "
                + mSharedPreferences.getString("servername", ""));

        listener = (item, pos) -> {
            Toast.makeText(this, "Server " + item.getDomain(), Toast.LENGTH_SHORT).show();
            //Log.d("DomainsViewModel dom ", item.getDomain());
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("servername", item.getDomain()).apply();
            editor.commit();
            finish();
        };
        mAdapter = new DomainsAdapter(listener);
        mRecyclerView.setAdapter(mAdapter);

        bindData();
        viewModel.onStart();

    }

    private void savedDomains(@NonNull final List<RealmDomain> domains) {

        //Log.d("DomainsViewModel dom ", domains.get(0).getDomain());
        mAdapter.setDomainItems(domains);
    }

    @OnClick(R.id.increment_button)
    public void incrementClickCount(View button) {
        viewModel.setCount(viewModel.getCount() + 1);
        displayClickCount(viewModel.getCount());
    }

    private void displayClickCount(int clickCount) {
        clickCountText.setText(String.valueOf(clickCount));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
        mSubscription.clear();
        viewModel.onDestroy();
    }


    public void bindData() {

        mSubscription = new CompositeSubscription();
        mSubscription.add(viewModel.getSavedDomainFromRealm()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error DomainsViewModel " + throwable.getMessage());
                    //hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::savedDomains));
    }




}
