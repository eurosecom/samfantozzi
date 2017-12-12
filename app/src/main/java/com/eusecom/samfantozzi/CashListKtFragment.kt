package com.eusecom.samfantozzi

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import com.eusecom.samfantozzi.models.Attendance
import com.eusecom.samfantozzi.rxbus.RxBus
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Kotlin fragment Recyclerview with classic XML itemlayout without Anko DSL

 */

class CashListKtFragment : Fragment() {

    private var mAdapter: CashListAdapter? = null
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
    protected var mSupplierSearchEngine: SupplierSearchEngine? = null
    var searchManager: SearchManager? = null

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

        mAdapter = CashListAdapter(_rxBus)
        mAdapter?.setAbsserver(emptyList())
        mSupplierSearchEngine = SupplierSearchEngine(emptyList())
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
                        //Log.d("CashListKtActivity  ", " fobClick ")
                        newCashDocDialog().show()

                    }
                    if (event is Invoice) {

                        val usnamex = event.nai

                        //Log.d("CashListKtFragment ", usnamex)
                        getTodoDialog(event)


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
        mSubscription?.add(mViewModel.getMyInvoicesFromSqlServer("3")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("CashListKtFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerInvoices(it) })

        mSubscription?.add(mViewModel.getObservableFromFBforRealm()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("CashListKtFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setFbAbsences(it) })

        mSubscription?.add(mViewModel.getObservableDocPdf()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("CashListKtFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setUriPdf(it) })

        mSubscription?.add(mViewModel.getMyObservableCashListQuery()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("CashListKtFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setQueryString(it) })


        ActivityCompat.invalidateOptionsMenu(activity)
        (activity as AppCompatActivity).supportActionBar!!.setTitle(mSharedPreferences.getString("ume", "") + " "
                + mSharedPreferences.getString("pokluce", "") + " " + getString(R.string.cashdocuments))
    }

    private fun unBind() {
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        _disposables.dispose()
        if (mDisposable != null) {
            mDisposable?.dispose()
        }
        mViewModel.clearObservableAbsencesFromFB()
        mViewModel.clearObservableDocPDF()
        mViewModel.clearObservableCashListQuery()
        hideProgressBar()

    }

    private fun setQueryString(querystring: String) {

        //toast(" querystring " + querystring)
        if( querystring.equals("")){

        }else {
            searchView?.setQuery(querystring, false)
        }

    }

    private fun setFbAbsences(absences: List<Attendance>) {

        toast(" absence0 " + absences.get(0).dmna)

    }

    private fun setServerInvoices(invoices: List<Invoice>) {

        //toast(" nai0 " + invoices.get(0).nai)
        mAdapter?.setAbsserver(invoices)
        nastavResultAs(invoices)
        hideProgressBar()
    }

    protected fun showResultAs(resultAs: List<Invoice>) {

        if (resultAs.isEmpty()) {
            //Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
            mAdapter?.setAbsserver(emptyList<Invoice>())
        } else {
            //Log.d("showResultAs ", resultAs.get(0).dmna);
            mAdapter?.setAbsserver(resultAs)
        }
    }

    protected fun nastavResultAs(resultAs: List<Invoice>) {
        mSupplierSearchEngine = SupplierSearchEngine(resultAs)
    }

    private fun setUriPdf(uri: Uri) {

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

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
        Log.d("CashListKtFragment ", "onDestroy");
        unBind()

    }

    override fun onResume() {
        super.onResume()
        Log.d("CashListKtFragment ", "onResume");
        bind()
    }

    override fun onPause() {
        super.onPause()
        Log.d("CashListKtFragment ", "onPause");
        unBind()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        // Retrieve the SearchView and plug it into SearchManager
        inflater!!.inflate(R.menu.menu_listdoc, menu)
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


    //listener to searchview
    private fun getObservableSearchViewText() {

        val searchViewChangeStream = createSearchViewTextChangeObservable()

        mDisposable = searchViewChangeStream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgressBar() }
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(Function<String, List<Invoice>> {
                    query -> mSupplierSearchEngine?.searchModel(query)
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
        valuex.text = invoice.hod

        val builder = AlertDialog.Builder(activity)
        builder.setView(textenter).setTitle(getString(R.string.document) + " " + invoice.dok)

        builder.setItems(arrayOf<CharSequence>(getString(R.string.pdf), getString(R.string.edit), getString(R.string.delete))
        ) { dialog, which ->
            // The 'which' argument contains the index position
            // of the selected item
            when (which) {
                0 -> {
                    mViewModel.emitDocumentPdfUri(invoice)
                }
                1 -> {
                    editDialog(invoice).show()
                }
                2 -> {
                    deleteDialog(invoice).show()
                }
            }
        }
        val dialog = builder.create()
        builder.show()

    }

    fun newCashDocDialog(): AlertDialogBuilder {

        alert = alert() {
            positiveButton(R.string.expense) { navigateToNewCashDoc(2) }
            neutralButton(R.string.receipt)  { navigateToNewCashDoc(1) }
        }
        val titlex: String = getString(R.string.createdoc)
        alert.title(titlex)

        return alert

    }

    fun navigateToNewCashDoc(drupoh: Int){

        //getActivity().startActivity<InvoiceListKtActivity>()
        val intent = Intent(getActivity(), FormValidationActivity::class.java)
        startActivity(intent)

    }

    fun editDialog(invoice: Invoice): AlertDialogBuilder {

        alert = alert() {
            positiveButton(R.string.edit) { navigateToEditDoc(invoice) }
            neutralButton(R.string.close)
        }
        val titlex: String = getString(R.string.editdoc) + " " + invoice.dok
        alert.title(titlex)

        return alert

    }

    fun navigateToEditDoc(invoice: Invoice){

        //getActivity().startActivity<InvoiceListKtActivity>()

    }

    fun deleteDialog(invoice: Invoice): AlertDialogBuilder {

        alert = alert() {
            positiveButton(R.string.delete) { navigateToDeleteDoc(invoice) }
            neutralButton(R.string.close)
        }
        val titlex: String = getString(R.string.deletedoc) + " " + invoice.dok
        alert.title(titlex)

        return alert

    }

    fun navigateToDeleteDoc(invoice: Invoice){

        //getActivity().startActivity<InvoiceListKtActivity>()

    }


}
