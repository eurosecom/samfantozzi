package com.eusecom.samfantozzi

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_android_version.view.*

class ChooseCompanyAdapter(val myAndroidOSList: ArrayList<ChooseCompanyData>, val listener: (ChooseCompanyData) -> Unit) : RecyclerView.Adapter<ChooseCompanyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseCompanyAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_android_version, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChooseCompanyAdapter.ViewHolder, position: Int) {
        holder.bindItems(myAndroidOSList[position], listener)
    }

    override fun getItemCount(): Int {
        return myAndroidOSList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(myAndroidOS: ChooseCompanyData, listener: (ChooseCompanyData) -> Unit) = with(itemView)  {

            itemView.tvName.text = myAndroidOS.name
            itemView.tvVersion.text = myAndroidOS.version
            itemView.ivIcon.setImageResource(myAndroidOS.imageIcon)
            itemView.setOnClickListener{listener(myAndroidOS)}

        }
    }


}
