package com.eusecom.samfantozzi

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.eusecom.samfantozzi.rxbus.RxBus
import org.jetbrains.anko.setContentView
import javax.inject.Inject

/**
 * Kotlin activity Recyclerview with classic XML itemlayout without Anko DSL

 */

class NewCashDocKtActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    @Inject
    lateinit var _rxBus: RxBus

    private var mPagerAdapter: FragmentPagerAdapter? = null
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SamfantozziApp).dgaeacomponent().inject(this)

        //setContentView(R.layout.activity_cashlist)
        NewCashDocKtActivityUI(_rxBus).setContentView(this)

        supportActionBar!!.setTitle(prefs.getString("pokluce", "") + " " +  getString(R.string.newdoc))

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            private val mFragments = arrayOf(NewCashDocFragment(), EmptyKtFragment())
            private val mFragmentNames = arrayOf(getString(R.string.newdoc), getString(R.string.empty))

            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return mFragmentNames[position]
            }
        }


        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById<View>(R.id.container) as ViewPager
        //mViewPager.setAdapter(mPagerAdapter) kotlin smart cast to ViewPager is impossible mPagerAdapter = null from other thread
        mViewPager?.setAdapter(mPagerAdapter)

            mViewPager?.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    // Check if this is the page you want.
                    if (position == 0) {
                        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
                        fab.visibility = View.GONE
                        supportActionBar!!.setTitle(prefs.getString("ume", "") + " " + getString(R.string.cashdocuments))
                    }
                    if (position == 1) {
                        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
                        fab.setVisibility(View.GONE);
                        //fab.visibility = View.VISIBLE
                        supportActionBar!!.setTitle(getString(R.string.empty))
                    }

                }
            })

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setVisibility(View.GONE);

    }


    override fun onDestroy() {
        super.onDestroy()
    }


}
