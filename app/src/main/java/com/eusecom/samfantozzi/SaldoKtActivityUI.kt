package com.eusecom.samfantozzi

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.eusecom.samfantozzi.rxbus.RxBus
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.support.v4.viewPager

class SaldoKtActivityUI (val _rxBus: RxBus, val prefs: SharedPreferences, val saltype: Int): AnkoComponent<SaldoKtActivity>{

    override fun createView(ui: AnkoContext<SaldoKtActivity>): View = with(ui){

        fun callCommandExecutorProxy(perm: String , dbType: AccountReportsHelperFacade.DBTypes
                                     , reportType :AccountReportsHelperFacade.ReportTypes
                                     , tableName : AccountReportsHelperFacade.ReportName
                                     , context: Context) {
            val executor = CommandExecutorProxy(prefs.getString("usuid", "0")
                    , prefs.getString("fir", "0"), prefs.getString("usadmin", "0"))
            try {
                executor.runCommand(perm, dbType, reportType, tableName, context)
            } catch (e: Exception) {
                println("Exception Message::" + e.message)
                if(e.message.equals("adm")) { ui.owner.showDonotadminAlert() }
                if(e.message.equals("lgn")) { ui.owner.showDonotloginAlert() }
                if(e.message.equals("cmp")) { ui.owner.showDonotcompanyAlert() }
            }
        }

        return relativeLayout{
            padding = dip(5)
            lparams (width = matchParent, height = wrapContent)

            verticalLayout{

                tabLayout{
                    lparams {
                    width = matchParent
                    height = wrapContent
                    }
                    id = R.id.tabs

                }

                viewPager{
                    lparams {
                    width = matchParent
                    height = matchParent
                    }
                    id = R.id.container

                }

            }

            floatingActionButton{

                imageResource = android.R.drawable.ic_input_add
                id = R.id.fab

                onClick{
                    _rxBus.send(IdcListKtFragment.ClickFobEvent())

                }

            }.lparams {
                width = wrapContent
                height = wrapContent
                alignParentBottom()
                alignParentRight()
            }

            bottomNavigationView {
                id = R.id.botnav
                inflateMenu(R.menu.saldo_bottommenu)
                itemBackgroundResource = R.color.colorPrimaryLight
                selectedItemId = R.id.action_toothersaldo

            }.lparams {
                width = matchParent
                height = wrapContent
                //gravity = Gravity.BOTTOM
                //android:layout_gravity="bottom"
                alignParentBottom()
            }.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_toothersaldo -> {
                        ui.owner.finishActivity("0")
                    }
                    R.id.action_saldopdf1 -> {

                        if(saltype == 0 ) {
                            //generating MySql PDF report with using CommandExecutorProxy and Facade
                            callCommandExecutorProxy("lgn", AccountReportsHelperFacade.DBTypes.MYSQL
                                    , AccountReportsHelperFacade.ReportTypes.PDF
                                    , AccountReportsHelperFacade.ReportName.SALIDC0, context)
                        }else{
                            callCommandExecutorProxy("lgn", AccountReportsHelperFacade.DBTypes.MYSQL
                                    , AccountReportsHelperFacade.ReportTypes.PDF
                                    , AccountReportsHelperFacade.ReportName.SALIDC1, context)
                        }
                    }
                    R.id.action_saldopdf2 -> {
                        //ui.owner.finishActivity("2")
                    }
                }

                false
            }

        }

    }



}