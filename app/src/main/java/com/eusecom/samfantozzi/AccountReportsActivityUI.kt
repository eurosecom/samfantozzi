package com.eusecom.samfantozzi

import android.view.View
import org.jetbrains.anko.*


class AccountReportsActivityUI (): AnkoComponent<AccountReportsActivity>{

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

            button() {
                lparams {
                    width = matchParent
                    height = wrapContent
                }
                textResource = R.string.popisbtnpenden
                onClick { /* Todo on click */ }
            }

            button() {
                lparams {
                    width = matchParent
                    height = wrapContent
                }
                textResource = R.string.popisbtnpenden2
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





        }

    }

}