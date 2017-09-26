package com.eusecom.samfantozzi

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.activity_main.*
import android.content.SharedPreferences
import android.support.v4.widget.DrawerLayout
import android.widget.Toast
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import javax.inject.Inject

/**
 * SamFantozzi Main activity
 * Kotlin and Java dagger 2.11
 */

class MainFantozziActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val serverx = prefs.getString("servername", "")
        Toast.makeText(this, serverx, Toast.LENGTH_SHORT).show()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            val intent = Intent(this, Detail2Activity::class.java)
            startActivity(intent)
        }

        //kotlin drawer by https://github.com/zsmb13/MaterialDrawerKt
        drawer {

            primaryItem("Home") {}
            divider {}
            primaryItem("Users") {}
            secondaryItem("Settings") {

                onClick { _ ->
                    //Log.d("DRAWER", "Click.")
                    navigateToSettings()
                    false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> consume { navigateToSettings() }

        else -> super.onOptionsItemSelected(item)
    }

    fun navigateToSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }


    //consume oncreateoptionmenu
    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }



}
