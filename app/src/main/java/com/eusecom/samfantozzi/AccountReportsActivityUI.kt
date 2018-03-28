package com.eusecom.samfantozzi

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.eusecom.samfantozzi.retrofit.AbsServerService
import org.jetbrains.anko.*


class AccountReportsActivityUI (val mReport: String, val mAbsServerService: AbsServerService): AnkoComponent<AccountReportsActivity>{

    override fun createView(ui: AnkoContext<AccountReportsActivity>): View = with(ui){

        return verticalLayout{
            padding = dip(10)
            lparams (width = matchParent, height = wrapContent)

            button() {
                lparams {
                    width = matchParent
                    height = wrapContent
                }
                textResource = R.string.action_setmonth
                onClick { startActivity<ChooseMonthActivity>() }
            }

            if( mReport.equals("0")) {

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
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
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnpenden2
                    onClick {

                        //generating MySql PDF report with using Facade
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.PENDEN2, context);
                    }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnprivyd
                    onClick {

                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.PRIVYD, context);

                    }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnmajzav
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.MAJZAV, context);
                    }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnkniodb
                    onClick { /* Todo on click */ }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnknidod
                    onClick { /* Todo on click */ }
                }

            }//report 0


            if( mReport.equals("1")) {

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtndph
                    onClick { /* Todo on click */ }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnprivyd
                    onClick { /* Todo on click */ }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnmajzav
                    onClick { /* Todo on click */ }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnfob
                    onClick { /* Todo on click */ }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnplatbystatu
                    onClick { /* Todo on click */ }
                }

            }//report 1


            if( mReport.equals("2")) {

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnvyppoh
                    onClick { /* Todo on click */ }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.popisbtnhladok
                    onClick { /* Todo on click */ }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.customers
                    onClick { /* Todo on click */ }
                }

                button() {
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    textResource = R.string.suppliers
                    onClick {

                    }
                }


            }//report 2



        }

    }

}