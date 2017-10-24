package com.eusecom.samfantozzi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import org.jetbrains.anko.toast

/**
 * Kotlin activity Recyclerview with classic XML itemlayout without Anko DSL
 * by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class ChooseCompanyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosecompany)

        //Bind the recyclerview
        val recyclerView = findViewById<RecyclerView>(R.id.rvAndroidVersions)

        //Add a LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.addItemDecoration(LinearLayoutSpaceItemDecoration(16))

        //Here we create an arraylist to store alChooseCompanyData using the data class ChooseCompanyData
        val alChooseCompanyData = ArrayList<ChooseCompanyData>()

        //Adding some data to the arraylist
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.cupcake,"Cupcake", "v1.5"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.donut1,"Donut", "v1.6"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.eclair,"Eclair", "v2.1"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.froyo,"Froyo", "v2.2.x"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.gingerbread,"Gingerbread", "v2.3.x"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.honeycomb,"Honeycomb", "v3.x"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.icecreamsandwich,"Ice Cream Sandwich", "v4.0.x"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.jellybean,"Jelly Bean", "v4.1.x"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.kitkat,"KitKat", "v4.4.x"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.lollipop,"Lollipop", "v5.0"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.marshmallow1,"Marshmallow", "v6.0"))
        alChooseCompanyData.add(ChooseCompanyData(R.drawable.nougat,"Nougat", "v7.0"))

        // adding the adapter to recyclerView
        recyclerView.adapter = ChooseCompanyAdapter(alChooseCompanyData){
            //Anko library has its own definition of toast which we have addded in build.gradle
            toast("${it.name + " " + it.version } Clicked")//A toast that displays the name of OS which you clicked on
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
