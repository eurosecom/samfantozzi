package com.eusecom.samfantozzi

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.eusecom.samfantozzi.models.Attendance
import com.eusecom.samfantozzi.rxbus.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.support.v4.toast
import rx.Observable
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Kotlin fragment Recyclerview with classic XML itemlayout without Anko DSL

 */

class CashListKtFragment : Fragment() {

    private var mAdapter: AbsServerAsAdapter? = null
    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null

    private var mProgressBar: ProgressBar? = null

    private var mSubscription: CompositeSubscription? = null
    private var _disposables = CompositeDisposable()

    @Inject
    lateinit var mSharedPreferences: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    @Inject
    lateinit var  _rxBus: RxBus

    //searchview
    private var searchView: SearchView? = null
    private var onQueryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity.application as SamfantozziApp).dgaeacomponent().inject(this)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_cashlistkt, container, false)

        mRecycler = rootView.findViewById<View>(R.id.list) as RecyclerView
        mRecycler?.setHasFixedSize(true)
        mProgressBar = rootView.findViewById<View>(R.id.progress_bar) as ProgressBar

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter = AbsServerAsAdapter(_rxBus)
        // Set up Layout Manager, reverse layout
        mManager = LinearLayoutManager(context)
        mManager?.setReverseLayout(true)
        mManager?.setStackFromEnd(true)
        mRecycler?.setLayoutManager(mManager)
        mRecycler?.setAdapter(mAdapter)

    }//end of onActivityCreated

    private fun bind() {

        _disposables = CompositeDisposable()

        val tapEventEmitter = _rxBus.asFlowable().publish()

        _disposables
                .add(tapEventEmitter.subscribe { event ->
                    if (event is CashListKtFragment.ClickFobEvent) {
                        Log.d("CashListKtActivity  ", " fobClick ")

                    }
                    if (event is Attendance) {

                        val usnamex = event.getUsname()

                        Log.d("CashListKtFragment ", usnamex)
                        //String serverx = "DgAeaListFragment " + usnamex;
                        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();


                    }

                })

        _disposables
                .add(tapEventEmitter.publish { stream -> stream.buffer(stream.debounce(1, TimeUnit.SECONDS)) }
                        .observeOn(AndroidSchedulers.mainThread()).subscribe { taps ->
                    ///_showTapCount(taps.size()); OK
                })

        _disposables.add(tapEventEmitter.connect())

        mSubscription = CompositeSubscription()

        mSubscription?.add(mViewModel.myAbsencesFromServer
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable -> Log.e("CashListKtFragment", "Error Throwable " + throwable.message) }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setAbsences(it) }))

        mSubscription?.add(mViewModel.getMyInvoicesFromSqlServer("1")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("CashListKtFragment", "Error Throwable " + throwable.message)
                    //hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerInvoices(it) })

        (activity as AppCompatActivity).supportActionBar!!.setTitle(mSharedPreferences.getString("ume", "") + " "
                + mSharedPreferences.getString("pokluce", "") + " " + getString(R.string.cashdocuments))
    }

    private fun unBind() {
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        _disposables.dispose()
        hideProgressBar()

    }

    private fun setAbsences(attendances: List<Attendance>) {
        mAdapter?.setAbsserver(attendances)
    }

    private fun setServerInvoices(invoices: List<Invoice>) {

        toast(" nai0 " + invoices.get(0).nai)

    }

    class ClickFobEvent

    protected fun showProgressBar() {
        mProgressBar?.setVisibility(View.VISIBLE)
    }

    protected fun hideProgressBar() {
        mProgressBar?.setVisibility(View.GONE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unBind()

    }

    override fun onResume() {
        super.onResume()
        bind()
    }

    override fun onPause() {
        super.onPause()
        unBind()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        // Retrieve the SearchView and plug it into SearchManager
        inflater!!.inflate(R.menu.menu_listdoc, menu)
        searchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as SearchView
        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
        //getObservableSearchViewText()
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if (id == R.id.action_settings) {

            val `is` = Intent(activity, SettingsActivity::class.java)
            startActivity(`is`)
            return true
        }

        if (id == R.id.action_setmonth) {

            val `is` = Intent(activity, ChooseMonthActivity::class.java)
            startActivity(`is`)
            return true
        }

        if (id == R.id.action_setaccount) {

            val `is` = Intent(activity, ChooseAccountActivity::class.java)
            val extras = Bundle()
            extras.putString("fromact", "3")
            `is`.putExtras(extras)
            startActivity(`is`)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
