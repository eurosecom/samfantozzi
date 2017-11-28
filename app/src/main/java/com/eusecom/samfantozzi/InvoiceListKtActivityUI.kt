package com.eusecom.samfantozzi

import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.eusecom.samfantozzi.rxbus.RxBus
import org.jetbrains.anko.*
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager


class InvoiceListKtActivityUI (val _rxBus: RxBus): AnkoComponent<InvoiceListKtActivity>{

    override fun createView(ui: AnkoContext<InvoiceListKtActivity>): View = with(ui){

        return relativeLayout{
            padding = dip(5)
            lparams {
                width = matchParent
                height = wrapContent
                margin = 5
            }

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
                    rightMargin = 10
                    bottomMargin = 10
                    alignParentBottom()
                    alignParentRight()
                }
                imageResource = android.R.drawable.ic_input_add
                id = R.id.fabinvoice
                onClick{
                    _rxBus.send(InvoiceListFragment.ClickFobEvent())
                }

            }

        }

    }

}