package com.eusecom.samfantozzi

import android.content.Context
import android.view.View
import org.jetbrains.anko.*


class ChooseMonthItemUI : AnkoComponent<Context> {

    override fun createView(ui: AnkoContext<Context>): View = with(ui){

        return relativeLayout {
            padding = dip(15)
            isClickable = true
            background = context.obtainStyledAttributes(arrayOf(R.attr.selectableItemBackground).toIntArray()).getDrawable(0)

            // i do not use onclick from AnkoComponent, i use Adapter listener
            //onClick{
            //    toast("Clicked onItem")
            //}

            imageView{
                lparams{
                    width = dip(35)
                    height = dip(35)
                    setMargins(0, 0, dip(15), 0)
                }
                id = R.id.mnimg

            }

            textView{
                lparams{
                    width = matchParent
                    height = wrapContent
                    rightOf(R.id.mnimg)
                }
                textSize = sp(9).toFloat()
                id = R.id.mnname

            }
            textView{
                lparams {
                    width = wrapContent
                    height = wrapContent
                    rightOf(R.id.mnimg)
                    below(R.id.mnname)
                }
                    textSize = sp(7).toFloat()
                    id = R.id.mnnumber
                }

            }


    }
}