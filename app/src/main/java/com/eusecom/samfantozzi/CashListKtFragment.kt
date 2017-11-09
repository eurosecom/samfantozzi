package com.eusecom.samfantozzi

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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

    protected var mQueryEditText: EditText? = null
    protected var mSearchButton: Button? = null
    private var mProgressBar: ProgressBar? = null

    private var mSubscription: CompositeSubscription? = null
    private var _disposables = CompositeDisposable()

    @Inject
    lateinit var mSharedPreferences: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    @Inject
    lateinit var  _rxBus: RxBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity.application as SamfantozziApp).dgaeacomponent().inject(this)

        val tapEventEmitter = _rxBus.asFlowable().publish()

        _disposables
                .add(tapEventEmitter.subscribe { event ->
                    if (event is DgAeaListFragment.ClickFobEvent) {
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
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.activity_absserver, container, false)

        mRecycler = rootView.findViewById<View>(R.id.list) as RecyclerView
        mRecycler?.setHasFixedSize(true)

        mQueryEditText = rootView.findViewById<View>(R.id.query_edit_text) as EditText
        mSearchButton = rootView.findViewById<View>(R.id.search_button) as Button
        mProgressBar = rootView.findViewById<View>(R.id.progress_bar) as ProgressBar

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //(activity.application as SamfantozziApp).dgaeacomponent().inject(this)

        val umex = mSharedPreferences.getString("ume", "")
        toast(" umex " + umex)
        mAdapter = AbsServerAsAdapter(_rxBus)
        // Set up Layout Manager, reverse layout
        mManager = LinearLayoutManager(context)
        mManager?.setReverseLayout(true)
        mManager?.setStackFromEnd(true)
        mRecycler?.setLayoutManager(mManager)
        mRecycler?.setAdapter(mAdapter)

    }//end of onActivityCreated

    override fun onResume() {
        super.onResume()
        bind()
    }

    override fun onPause() {
        super.onPause()
        unBind()
    }

    private fun bind() {
        mSubscription = CompositeSubscription()

        mSubscription?.add(mViewModel.myAbsencesFromServer
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable -> Log.e("CashListKtFragment", "Error Throwable " + throwable.message) }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setAbsences(it) }))

    }

    private fun unBind() {
        mSubscription?.unsubscribe()
        mSubscription?.clear()

    }

    private fun setAbsences(attendances: List<Attendance>) {
        mAdapter?.setAbsserver(attendances)
    }

    override fun onDestroy() {
        super.onDestroy()
        //_disposables.dispose()
        //if (mDisposable != null) {
        //    mDisposable.dispose()
        //}
        mAdapter = AbsServerAsAdapter(_rxBus)
        mSubscription?.unsubscribe()
        mSubscription?.clear()

    }


}
