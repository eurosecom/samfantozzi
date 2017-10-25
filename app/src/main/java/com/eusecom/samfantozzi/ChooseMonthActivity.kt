package com.eusecom.samfantozzi

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import javax.inject.Inject
import android.R.id.edit



/**
 * Kotlin activity with ANKO Recyclerview with listener in adapter
 * by https://medium.com/@BhaskerShrestha/kotlin-and-anko-for-your-android-1c11054dd255
 * listener by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class ChooseMonthActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SamfantozziApp).dgaeacomponent().inject(this)

        val adapter: ChooseMonthAdapter = ChooseMonthAdapter(ArrayList<Month>()){
            toast("${it.monthsname + " " + it.monthsnumber } Clicked")
            val editor = prefs.edit()
            editor.putString("ume", it.monthsnumber).apply();
            editor.commit();
        }
        ChooseMonthActivityUI(adapter).setContentView(this)
        setData(adapter)

    }

    fun setData(adapter:ChooseMonthAdapter) {

        val rokx = prefs.getString("rok", "0")
        val alMyMonth = ArrayList<Month>()
        alMyMonth.add(Month(getString(R.string.january), "01." + rokx, "1"))
        alMyMonth.add(Month(getString(R.string.february), "02." + rokx, "2"))
        alMyMonth.add(Month(getString(R.string.march), "03." + rokx, "3"))
        alMyMonth.add(Month(getString(R.string.april), "04." + rokx, "4"))
        alMyMonth.add(Month(getString(R.string.may), "05." + rokx, "5"))
        alMyMonth.add(Month(getString(R.string.june), "06." + rokx, "6"))
        alMyMonth.add(Month(getString(R.string.july), "07." + rokx, "7"))
        alMyMonth.add(Month(getString(R.string.august), "08." + rokx, "8"))
        alMyMonth.add(Month(getString(R.string.september), "09." + rokx, "9"))
        alMyMonth.add(Month(getString(R.string.october), "10." + rokx, "10"))
        alMyMonth.add(Month(getString(R.string.november), "11." + rokx, "11"))
        alMyMonth.add(Month(getString(R.string.december), "12." + rokx, "12"))
        adapter.setdata(alMyMonth)

    }
}