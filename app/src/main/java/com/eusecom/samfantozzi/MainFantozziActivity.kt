package com.eusecom.samfantozzi

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_mainfantozzi.*
import android.content.SharedPreferences
import android.widget.Toast
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import co.zsmb.materialdrawerkt.draweritems.profile.profileSetting
import co.zsmb.materialdrawerkt.draweritems.sectionHeader
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.content_mainfantozzi.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

/**
 * SamFantozzi Main activity
 * Kotlin and Java dagger 2.11
 */

class MainFantozziActivity : AppCompatActivity() {

    private lateinit var result: Drawer
    private lateinit var headerResult: AccountHeader

    @Inject
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainfantozzi)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)

        //val serverx = prefs.getString("servername", "")
        //Toast.makeText(this, serverx, Toast.LENGTH_SHORT).show()
        val usermailx = prefs.getString("username", "")

        fab.setOnClickListener {

            //view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            _ -> navigateToCashList()
        }

        button1.setOnClickListener {
            _ -> navigateToCashList()
        }

        button2.setOnClickListener {
            _ -> navigateToAbsServer()
        }

        buttonFir.setText(getString(R.string.company) + " " + prefs.getString("fir", "") +
                " " + prefs.getString("firnaz", ""))
        buttonFir.setOnClickListener {
            _ -> navigateToGetCompany()
        }


        //kotlin drawer by https://github.com/zsmb13/MaterialDrawerKt
        result = drawer {

            toolbar = this@MainFantozziActivity.toolbar
            hasStableIds = true
            savedInstance = savedInstanceState
            showOnFirstLaunch = false


            headerResult = accountHeader {
                background = R.drawable.pozadie
                savedInstance = savedInstanceState
                translucentStatusBar = true

                profile("EuroSecom", usermailx) {
                    iconUrl = "http://www.edcom.sk"
                    identifier = 100
                }
                //profile("EDcom", "andrejd@edcom.sk") {
                //    iconUrl = "http://www.edcom.sk"
                //    identifier = 101
                //}

                profileSetting(getString(R.string.addacount_title), getString(R.string.addacount_summary)) {
                    icon = R.drawable.ic_history_black_24dp
                    identifier = 100_000

                    onClick { _ ->
                        navigateToLogin()
                        false
                    }

                }
                profileSetting(getString(R.string.resetpass_title), getString(R.string.resetpass_summary)) {
                    //iicon = GoogleMaterial.Icon.gmd_settings
                    icon = R.drawable.ic_check_circle_black_24dp
                    identifier = 100_001
                }


            }

            sectionHeader(getString(R.string.app_desc)) {
                divider = false
            }

            divider {}
            primaryItem(getString(R.string.action_loginout)) {

                onClick { _ ->
                    //Log.d("DRAWER", "Click.")
                    navigateToLogin()
                    false
                }

            }
            divider {}
            primaryItem(getString(R.string.action_accountreports)) {

                onClick { _ ->
                    //Log.d("DRAWER", "Click.")
                    //navigateToCashList()
                    false
                }

            }
            divider {}
            primaryItem(getString(R.string.action_vatreports)) {}
            divider {}
            primaryItem(getString(R.string.action_incometaxreports)) {}
            divider {}
            secondaryItem(getString(R.string.action_settings)) {

                onClick { _ ->
                    //Log.d("DRAWER", "Click.")
                    navigateToSettings()
                    false
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        result.saveInstanceState(outState)
        headerResult.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (result.isDrawerOpen)
            result.closeDrawer()
        else
            super.onBackPressed()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> consume { navigateToSettings() }
        R.id.action_setmonth -> consume { navigateToSetMonth() }

        else -> super.onOptionsItemSelected(item)
    }

    fun navigateToGetCompany(){
        startActivity<ChooseCompanyActivity>()
    }

    fun navigateToSetMonth(){
        startActivity<ChooseMonthActivity>()
    }

    fun navigateToSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun navigateToLogin(){
        val intent = Intent(this, EmailPasswordActivity::class.java)
        startActivity(intent)
    }

    fun navigateToCashList(){
        val intent = Intent(this, DgAeaActivity::class.java)
        startActivity(intent)
    }

    fun navigateToAbsServer(){
        val intent = Intent(this, AbsServerAsActivity::class.java)
        startActivity(intent)
    }


    //consume oncreateoptionmenu
    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }



}
