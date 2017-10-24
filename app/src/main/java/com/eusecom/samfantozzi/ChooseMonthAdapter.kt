package com.eusecom.samfantozzi

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eusecom.samfantozzi.Month
import org.jetbrains.anko.AnkoContext


class ChooseMonthAdapter(var mList: MutableList<Month>, val listener: (Month) -> Unit) : RecyclerView.Adapter<ChooseMonthAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindItem(mList[position], listener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(ChooseMonthItemUI().createView(AnkoContext.create(parent!!.context)))
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        val txtName = itemView?.findViewById<TextView>(R.id.mnname)
        val txtNumber = itemView?.findViewById<TextView>(R.id.mnnumber)

        fun bindItem(month: Month, listener: (Month) -> Unit) = with(itemView) {

            txtName?.setText(month.monthsname)
            txtNumber?.setText(month.monthsnumber)
            itemView.setOnClickListener{listener(month)}

        }
    }

    fun setdata(list: List<Month>){
        mList = list as MutableList<Month>
        notifyDataSetChanged()
    }
}