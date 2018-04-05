package com.eusecom.samfantozzi

import android.view.View
import com.eusecom.samfantozzi.rxbus.RxBus
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.support.v4.viewPager

class SaldoKtActivityUI (val _rxBus: RxBus): AnkoComponent<SaldoKtActivity>{

    override fun createView(ui: AnkoContext<SaldoKtActivity>): View = with(ui){

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
                        //ui.owner.finishActivity("1")
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