package com.eusecom.samfantozzi

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.eusecom.samfantozzi.models.Employee
import org.jetbrains.anko.toast
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Kotlin activity Recyclerview with classic XML itemlayout without Anko DSL
 * by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class ChooseCompanyActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    var mSubscription: CompositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SamfantozziApp).dgaeacomponent().inject(this)

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
            toast("${it.name + " " + it.version } Clicked")
            val editor = prefs.edit()
            editor.putString("fir", it.name).apply();
            editor.putString("firnaz", it.version).apply();
            editor.commit();
            val i = intent
            setResult(101, i)
            finish()
        }

        bind();

    }

    private fun bind() {

        mSubscription.add(mViewModel.observableFBusersRealmEmployee
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe({ it -> setEmployees(it) }))

    }

    private fun setEmployees(employees: List<Employee>) {

        toast("${employees.get(0).username } month0")

    }

    override fun onDestroy() {
        super.onDestroy()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
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
