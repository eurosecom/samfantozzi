package com.eusecom.samfantozzi

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import com.eusecom.samfantozzi.rxbus.RxBus
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.support.v4.toast
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Kotlin fragment Recyclerview with classic XML itemlayout without Anko DSL

 */

abstract class SaldoListKtFragment : Fragment() {

    private var mAdapter: SaldoAdapter? = null
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

    private lateinit var alert: AlertDialogBuilder

    //searchview
    private var searchView: SearchView? = null
    private var onQueryTextListener: SearchView.OnQueryTextListener? = null
    private var mDisposable: Disposable? = null
    protected var mSaldoSearchEngine: SaldoSearchEngine? = null
    var searchManager: SearchManager? = null
    var saltype: Int = 0;
    var salico: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity.application as SamfantozziApp).dgaeacomponent().inject(this)
        setHasOptionsMenu(true)

    }

    abstract fun getSaldoType(drh: String): Int

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_acclistkt, container, false)

        mRecycler = rootView.findViewById<View>(R.id.list) as RecyclerView
        mRecycler?.setHasFixedSize(true)
        mProgressBar = rootView.findViewById<View>(R.id.progress_bar) as ProgressBar

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        salico = mSharedPreferences.getString("edidok", "").toInt()

        saltype= getSaldoType("xxx")
        mAdapter = SaldoAdapter(_rxBus, salico)
        mAdapter?.setAbsserver(emptyList())
        mSaldoSearchEngine = SaldoSearchEngine(emptyList())
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
                    if (event is SaldoListKtFragment.ClickFobEvent) {
                        Log.d("SaldoListKtKtActivity  ", " fobClick ")
                        //newCashDocDialog().show()

                    }
                    if (event is Invoice) {

                        val usnamex = event.nai

                        Log.d("SaldoListKtFragment ", usnamex)
                        if( salico == 0 ) {
                            getTodoDialog(event)
                        }else{
                            getTodoIdcDialog(event)
                        }


                    }

                })

        _disposables
                .add(tapEventEmitter.publish { stream -> stream.buffer(stream.debounce(1, TimeUnit.SECONDS)) }
                        .observeOn(AndroidSchedulers.mainThread()).subscribe { taps ->
                    ///_showTapCount(taps.size()); OK
                })

        _disposables.add(tapEventEmitter.connect())

        mSubscription = CompositeSubscription()

        showProgressBar()
        mSubscription?.add(mViewModel.getMySaldoFromSqlServer(getSaldoType("xxx"), salico)
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("AutopohListKtFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setPohyby(it) })

        mSubscription?.add(mViewModel.getMyObservableCashListQuery()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("AutoPohListKtFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setQueryString(it) })


    }

    private fun unBind() {

        mViewModel.clearObservableCashListQuery()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        _disposables.dispose()
        if (mDisposable != null) {
            mDisposable?.dispose()
        }

        hideProgressBar()

    }

    private fun setQueryString(querystring: String) {

        //toast(" querystring " + querystring)
        if( querystring.equals("")){

        }else {
            searchView?.setQuery(querystring, false)
        }

    }


    private fun setPohyby( pohyby: List<Invoice>) {
        //toast(" pohyb0 " + pohyby.get(0).nai)
        mAdapter?.setAbsserver(pohyby)
        nastavResultAs(pohyby)
        hideProgressBar()
    }


    protected fun showResultAs(resultAs: List<Invoice>) {

        if (resultAs.isEmpty()) {
            mAdapter?.setAbsserver(emptyList<Invoice>())
        } else {
            mAdapter?.setAbsserver(resultAs)
        }
    }

    protected fun nastavResultAs(resultAs: List<Invoice>) {
        mSaldoSearchEngine = SaldoSearchEngine(resultAs)
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
        //Log.d("SaldoListKtFragment ", "onDestroy");
        unBind()

    }

    override fun onResume() {
        super.onResume()
        //Log.d("SaldoListKtFragment ", "onResume");
        bind()
    }

    override fun onPause() {
        super.onPause()
        //Log.d("SaldoListKtFragment ", "onPause");
        unBind()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        // Retrieve the SearchView and plug it into SearchManager
        inflater!!.inflate(R.menu.menu_saldo, menu)
        searchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as SearchView
        searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity.componentName))
        getObservableSearchViewText()
    }

    override fun onDestroyOptionsMenu() {
        searchView?.setOnQueryTextListener(null)
        searchManager = null
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if (id == R.id.action_settings) {

            val `is` = Intent(activity, SettingsActivity::class.java)
            startActivity(`is`)
            return true
        }

        if (id == R.id.action_setaccount) {

            val `is` = Intent(activity, ChooseAccountActivity::class.java)
            val extras = Bundle()
            if( saltype == 0 ) { extras.putString("fromact", "1") }
            if( saltype == 1 ) { extras.putString("fromact", "2") }
            `is`.putExtras(extras)
            startActivity(`is`)
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    //listener to searchview
    private fun getObservableSearchViewText() {

        val searchViewChangeStream = createSearchViewTextChangeObservable()

        mDisposable = searchViewChangeStream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgressBar() }
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(Function<String, List<Invoice>> {
                    query -> mSaldoSearchEngine?.searchModel(query)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    hideProgressBar()
                    showResultAs(result)
                }

    }


    private fun createSearchViewTextChangeObservable(): io.reactivex.Observable<String> {
        val searchViewTextChangeObservable = io.reactivex.Observable.create(ObservableOnSubscribe<String> { emitter ->
            onQueryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // use this method when query submitted
                    //Toast.makeText(getActivity(), "submit " + query, Toast.LENGTH_SHORT).show();
                    emitter.onNext(query.toString())
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // use this method for auto complete search process
                    //Toast.makeText(getActivity(), "change " + newText, Toast.LENGTH_SHORT).show();
                    emitter.onNext(newText.toString())
                    mViewModel.emitMyObservableCashListQuery(newText.toString())
                    return false
                }


            }

            searchView?.setOnQueryTextListener(onQueryTextListener)

            emitter.setCancellable { searchView?.setOnQueryTextListener(null) }
        })

        return searchViewTextChangeObservable
                .filter { query -> query.length >= 3 || query == "" }.debounce(300, TimeUnit.MILLISECONDS)  // add this line
    }

    fun getTodoDialog(invoice: Invoice) {

        val inflater = LayoutInflater.from(activity)
        val textenter = inflater.inflate(R.layout.invoice_edit_dialog, null)

        val valuex = textenter.findViewById<View>(R.id.valuex) as TextView
        valuex.text = invoice.nai

        val builder = AlertDialog.Builder(activity)
        builder.setView(textenter).setTitle(getString(R.string.idc) + " " + invoice.ico)

        var item4: CharSequence = ""
        if( saltype == 0 ){
            item4=getString(R.string.reminder)
        }else{
            item4=getString(R.string.pay)
        }

        builder.setItems(arrayOf<CharSequence>(getString(R.string.detailidc), getString(R.string.action_saldopdf1)
                , getString(R.string.action_saldopdf2), item4)
        ) { dialog, which ->
            // The 'which' argument contains the index position
            // of the selected item
            when (which) {
                0 -> {
                    val `is` = Intent(activity, SaldoKtActivity::class.java)
                    val extras = Bundle()
                    extras.putInt("saltype", saltype)
                    extras.putInt("salico", invoice.ico.toInt())
                    `is`.putExtras(extras)
                    startActivity(`is`)
                }
                1 -> {

                    val `is` = Intent(activity, ShowPdfActivity::class.java)
                    val extras = Bundle()
                    if( saltype == 0 ) {
                        extras.putString("fromact", "71")
                        extras.putString("drhx", "71")
                    }else{
                        extras.putString("fromact", "72")
                        extras.putString("drhx", "72")
                    }
                    extras.putString("ucex", invoice.uce)
                    extras.putString("dokx", "0")
                    extras.putString("icox", invoice.ico)
                    `is`.putExtras(extras)
                    startActivity(`is`)
                    `is`.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                2 -> {

                    val `is2` = Intent(activity, ShowPdfActivity::class.java)
                    val extras2 = Bundle()
                    if( saltype == 0 ) {
                        extras2.putString("fromact", "73")
                        extras2.putString("drhx", "73")
                    }else{
                        extras2.putString("fromact", "74")
                        extras2.putString("drhx", "74")
                    }
                    extras2.putString("ucex", invoice.uce)
                    extras2.putString("dokx", "0")
                    extras2.putString("icox", invoice.ico)
                    `is2`.putExtras(extras2)
                    startActivity(`is2`)
                    `is2`.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                3 -> {

                }
            }
        }
        val dialog = builder.create()
        builder.show()

    }

    fun getTodoIdcDialog(invoice: Invoice) {

        val inflater = LayoutInflater.from(activity)
        val textenter = inflater.inflate(R.layout.invoice_edit_dialog, null)

        val valuex = textenter.findViewById<View>(R.id.valuex) as TextView
        valuex.text = invoice.nai

        val builder = AlertDialog.Builder(activity)
        builder.setView(textenter).setTitle(getString(R.string.invoice) + " " + invoice.fak)

        var item4: CharSequence = ""
        if( saltype == 0 ){
            item4=getString(R.string.reminder)
        }else{
            item4=getString(R.string.pay)
        }

        builder.setItems(arrayOf<CharSequence>(getString(R.string.showpdf), item4)
        ) { dialog, which ->
            // The 'which' argument contains the index position
            // of the selected item
            when (which) {
                0 -> {
                    val `is` = Intent(activity, SaldoKtActivity::class.java)
                    val extras = Bundle()
                    extras.putInt("saltype", saltype)
                    extras.putInt("salico", invoice.ico.toInt())
                    `is`.putExtras(extras)
                    //startActivity(`is`)
                }
                1 -> {

                }
            }
        }
        val dialog = builder.create()
        builder.show()

    }


}
