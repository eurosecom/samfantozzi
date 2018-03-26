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
import com.eusecom.samfantozzi.retrofit.AbsServerService
import io.realm.annotations.PrimaryKey
import org.jetbrains.anko.support.v4.toast
import rx.Observable


/**
 * Kotlin activity with ANKO Recyclerview
 * by https://medium.com/@BhaskerShrestha/kotlin-and-anko-for-your-android-1c11054dd255
 * listener by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class AccountReportsActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mAbsServerService: AbsServerService

    var reports: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SamfantozziApp).dgaeacomponent().inject(this)

        val i = intent
        val extras = i.extras
        //0 accounting, 1 vat, 2 income, 3 mixed
        reports = extras!!.getString("reports")

        AccountReportsActivityUI(reports, mAbsServerService).setContentView(this)


    }

    override fun onResume() {
        super.onResume()

        val umex = prefs.getString("ume", "0")
        if( reports.equals("0")) { supportActionBar!!.setTitle(umex + " " + getString(R.string.accrep)) }
        if( reports.equals("1")) { supportActionBar!!.setTitle(umex + " " + getString(R.string.action_taxreports)) }
        if( reports.equals("2")) { supportActionBar!!.setTitle(umex + " " + getString(R.string.mixedreports)) }
    }

    override fun onDestroy() {
        super.onDestroy()


    }



}
