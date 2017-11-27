package com.eusecom.samfantozzi

import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager


class InvoiceListKtActivityUI (): AnkoComponent<InvoiceListKtActivity>{

    override fun createView(ui: AnkoContext<InvoiceListKtActivity>): View = with(ui){

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
                lparams {
                    width = wrapContent
                    height = wrapContent
                    alignParentBottom()
                    alignParentRight()
                }
                imageResource = android.R.drawable.ic_input_add
                id = R.id.fab

            }

        }

    }

}