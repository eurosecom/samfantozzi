package com.eusecom.samfantozzi

import android.content.SharedPreferences
import android.view.Gravity
import android.view.View
import com.eusecom.samfantozzi.retrofit.AbsServerService
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.sdk25.listeners.onClick


class AccountReportsActivityUI (val mReport: String, val prefs: SharedPreferences): AnkoComponent<AccountReportsActivity>{

    override fun createView(ui: AnkoContext<AccountReportsActivity>): View = with(ui){

        return relativeLayout{
            padding = dip(10)
            lparams (width = matchParent, height = matchParent)

            button() {
                id = R.id.rep00
                textResource = R.string.action_setmonth
                onClick { startActivity<ChooseMonthActivity>() }
            }.lparams {
                width = matchParent
                height = wrapContent
                top
            }

            if( mReport.equals("0")) {

                button() {
                    id = R.id.rep01
                    textResource = R.string.popisbtnpenden
                    onClick {
                        //generating MySql PDF report without using Facade
                        //val con = AccountReportsMySqlHelper.getMySqlDBConnection()
                        //val mySqlHelper = AccountReportsMySqlHelper()
                        //mySqlHelper.generateMySqlPDFReport("penden", mAbsServerService)

                        //generating MySql PDF report with using Facade
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.PENDEN, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep00)
                }

                button() {
                    id = R.id.rep02
                    textResource = R.string.popisbtnpenden2
                    onClick {

                        //generating MySql PDF report with using Facade
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.PENDEN2, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep01)
                }

                button() {
                    id = R.id.rep03
                    textResource = R.string.popisbtnprivyd
                    onClick {

                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.PRIVYD, context);

                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep02)
                }

                button() {
                    id = R.id.rep04
                    textResource = R.string.popisbtnmajzav
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.MAJZAV, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep03)
                }

                button() {
                    id = R.id.rep05
                    textResource = R.string.popisbtnkniodb
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.KNIODB, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep04)
                }

                button() {
                    id = R.id.rep06
                    textResource = R.string.popisbtnknidod
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.KNIDOD, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep05)
                }

            }//report 0


            if( mReport.equals("1")) {

                button() {
                    id = R.id.rep11
                    textResource = R.string.popisbtndph
                    onClick { /* Todo on click */ }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep00)
                }

                button() {
                    id = R.id.rep12
                    textResource = R.string.popisbtnfinsta
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.FINSTA, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep11)
                }

                button() {
                    id = R.id.rep13
                    textResource = R.string.popisbtnfob
                    onClick { /* Todo on click */ }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep12)
                }

                button() {
                    id = R.id.rep14
                    textResource = R.string.popisbtnplatbystatu
                    onClick { /* Todo on click */ }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep13)
                }

            }//report 1


            if( mReport.equals("2")) {

                button() {
                    id = R.id.rep21
                    textResource = R.string.popisbtnvyppoh
                    onClick {

                        //http@ //www.eshoptest.sk/ucto/juknihapoh.php?h_obdp=1&h_obdk=1&copern=11&drupoh=2&page=1&typ=HTML#

                        val executor = CommandExecutorProxy(prefs.getString("usuid", "0")
                                , prefs.getString("fir", "0"), prefs.getString("usadmin", "0"))
                        try {
                            executor.runCommand("rm", AccountReportsHelperFacade.DBTypes.MYSQL
                                    , AccountReportsHelperFacade.ReportTypes.PDF
                                    , AccountReportsHelperFacade.ReportName.UCTPOH, context)
                        } catch (e: Exception) {
                            println("Exception Message::" + e.message)
                        }

                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep00)
                }

                button() {
                    id = R.id.rep22
                    textResource = R.string.popisbtnhladok
                    onClick { /* Todo on click */ }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep21)
                }

                button() {
                    id = R.id.rep23
                    textResource = R.string.customers
                    onClick { /* Todo on click */ }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep22)
                }

                button() {
                    id = R.id.rep24
                    textResource = R.string.suppliers
                    onClick { /* Todo on click */ }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep23)
                }


            }//report 2

            bottomNavigationView {
                id = R.id.botnav
                inflateMenu(R.menu.reports_menu)
                itemBackgroundResource = R.color.colorPrimaryLight
                if( mReport.equals("0")) {selectedItemId = R.id.action_accounts}
                if( mReport.equals("1")) {selectedItemId = R.id.action_tax}
                if( mReport.equals("2")) {selectedItemId = R.id.action_mixed}
            }.lparams {
                width = matchParent
                height = wrapContent
                //gravity = Gravity.BOTTOM
                //android:layout_gravity="bottom"
                alignParentBottom()
            }.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_accounts -> {
                        ui.owner.finishActivity("0")
                    }
                    R.id.action_tax -> {
                        ui.owner.finishActivity("1")
                    }
                    R.id.action_mixed -> {
                        ui.owner.finishActivity("2")
                    }
                }

                false
            }

        }

    }



}