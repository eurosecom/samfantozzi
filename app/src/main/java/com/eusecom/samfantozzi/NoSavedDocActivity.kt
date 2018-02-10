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
import com.eusecom.samfantozzi.realm.RealmEmployee
import com.eusecom.samfantozzi.realm.RealmInvoice
import rx.Observable


/**
 * Kotlin activity with ANKO Recyclerview with listener in adapter
 * by https://medium.com/@BhaskerShrestha/kotlin-and-anko-for-your-android-1c11054dd255
 * listener by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class NoSavedDocActivity : AppCompatActivity() {

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

        //1=customers invoice, 2=supliers invoice, 3=cash document, 4=bank document, 5=internal document
        val extras = i.extras
        fromact = extras!!.getString("fromact")
        //toast("fromact " + fromact)

        val adapter: NoSavedDocAdapter = NoSavedDocAdapter(ArrayList<RealmInvoice>()){
            toast("${it.nai + " " + it.dok } set")

            finish()
        }
        NoSavedDocActivityUI(adapter).setContentView(this)

        supportActionBar!!.setTitle(getString(R.string.action_nosaveddoc))

        bind(adapter)

    }

    private fun bind(adapter: NoSavedDocAdapter) {

            mSubscription.add(mViewModel.getNoSavedDocFromRealm(fromact)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError { throwable -> Log.e("NoSavedDocAktivity ", "Error Throwable " + throwable.message) }
                    .onErrorResumeNext({ throwable -> Observable.empty() })
                    .subscribe({ it -> setNoSavedDocs(it, adapter) }))



    }


    private fun setNoSavedDocs(nosaveds: List<RealmInvoice>, adapter: NoSavedDocAdapter) {

        //toast("${nosaveds.get(0).dok } realminvoicedoc0")
        adapter.setdata(nosaveds)
    }


    override fun onDestroy() {
        super.onDestroy()
        mSubscription.unsubscribe()
        mSubscription.clear()
    }


}
