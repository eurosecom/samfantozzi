package com.eusecom.samfantozzi

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import javax.inject.Inject
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import android.util.Log
import rx.Observable


/**
 * Kotlin activity with ANKO Recyclerview with listener in adapter
 * by https://medium.com/@BhaskerShrestha/kotlin-and-anko-for-your-android-1c11054dd255
 * listener by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class ChooseAccountActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    var mSubscription: CompositeSubscription = CompositeSubscription()

    var fromact: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SamfantozziApp).dgaeacomponent().inject(this)

        val i = intent

        val extras = i.extras
        fromact = extras!!.getString("fromact")
        //toast("fromact " + fromact)

        val adapter: ChooseAccountAdapter = ChooseAccountAdapter(ArrayList<Account>()){
            toast("${it.accname + " " + it.accnumber } set")
            val editor = prefs.edit()
            editor.putString("doduce", it.accnumber).apply();
            editor.commit();
            finish()
        }
        ChooseAccountActivityUI(adapter).setContentView(this)

        supportActionBar!!.setTitle(getString(R.string.chooseaccount))

        bind(adapter)

    }

    private fun bind(adapter: ChooseAccountAdapter) {

            mSubscription.add(mViewModel.accounts
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError { throwable -> Log.e("ChooseAccountAktivity ", "Error Throwable " + throwable.message) }
                    .onErrorResumeNext({ throwable -> Observable.empty() })
                    .subscribe({ it -> setAccounts(it, adapter) }))

            mSubscription.add(mViewModel.getMyAccountsFromSqlServer(fromact)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError { throwable -> Log.e("ChooseAccountAktivity ", "Error Throwable " + throwable.message) }
                    .onErrorResumeNext({ throwable -> Observable.empty() })
                    .subscribe({ it -> setAccountsFromServer(it, adapter) }))

    }


    private fun setAccounts(accounts: List<Account>, adapter: ChooseAccountAdapter) {

        //toast("${accounts.get(0).accname } account0")
        adapter.setdata(accounts)
    }

    private fun setAccountsFromServer(invoices: List<Invoice>, adapter: ChooseAccountAdapter) {

        toast("${invoices.get(0).nai } invoice0")
        //adapter.setdata(invoices)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSubscription.unsubscribe()
        mSubscription.clear()
    }


}
