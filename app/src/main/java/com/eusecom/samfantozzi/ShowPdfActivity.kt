package com.eusecom.samfantozzi

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import javax.inject.Inject
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.eusecom.samfantozzi.realm.RealmEmployee
import com.eusecom.samfantozzi.realm.RealmInvoice
import io.realm.annotations.PrimaryKey
import org.jetbrains.anko.support.v4.toast
import rx.Observable


/**
 * Kotlin activity with ANKO Recyclerview
 * by https://medium.com/@BhaskerShrestha/kotlin-and-anko-for-your-android-1c11054dd255
 * listener by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class ShowPdfActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    var mSubscription: CompositeSubscription = CompositeSubscription()

    var fromact: String = "0"
    var drhx: String = "0"
    var ucex: String = "0"
    var dokx: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SamfantozziApp).dgaeacomponent().inject(this)

        val i = intent

        //1=customers invoice, 2=supliers invoice, 3=cash document, 4=bank document, 5=internal document
        val extras = i.extras
        fromact = extras!!.getString("fromact")
        drhx = extras!!.getString("drhx")
        ucex = extras!!.getString("ucex")
        dokx = extras!!.getString("dokx")

        val adapter: NoSavedDocAdapter = NoSavedDocAdapter(ArrayList<RealmInvoice>()){
            //toast("${it.nai + " " + it.dok } set")

        }
        ShowPdfActivityUI(adapter).setContentView(this)

        supportActionBar!!.setTitle(getString(R.string.showpdf))

        bind(adapter)

        val invoicex = Invoice(drhx,ucex,dokx,"","","","","","","",""
                ,"","","","","","","","",true,0,"","","")

        mViewModel.emitDocumentPdfUri(invoicex)

    }

    private fun bind(adapter: NoSavedDocAdapter) {

        mSubscription.add(mViewModel.getObservableDocPdf()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("CashListKtFragment", "Error Throwable " + throwable.message)
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setUriPdf(it) })


    }

    private fun setUriPdf(uri: Uri) {

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()

    }


    override fun onDestroy() {
        super.onDestroy()
        mSubscription.unsubscribe()
        mSubscription.clear()
        mViewModel.clearObservableDocPDF()
    }



}