package com.eusecom.samfantozzi

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import org.jetbrains.anko.*


class NoSavedDocItemUI : AnkoComponent<Context> {

    override fun createView(ui: AnkoContext<Context>): View = with(ui){

        return relativeLayout {
            padding = dip(5)
            isClickable = true
            //background = context.obtainStyledAttributes(arrayOf(R.attr.selectableItemBackground).toIntArray()).getDrawable(0)
            background = ResourcesCompat.getDrawable(resources, R.drawable.selector_medium_high, null)

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
                id = R.id.accimg

            }

            textView{
                lparams{
                    width = matchParent
                    height = wrapContent
                    rightOf(R.id.accimg)
                }
                textSize = sp(9).toFloat()
                id = R.id.accname

            }
            textView{
                lparams {
                    width = wrapContent
                    height = wrapContent
                    rightOf(R.id.accimg)
                    below(R.id.accname)
                }
                    textSize = sp(7).toFloat()
                    id = R.id.accnumber
                }

            }


    }
}