package com.blooburn.owere.user.adapter.userReservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.blooburn.owere.R

import com.blooburn.owere.user.item.ReservationListItem

class ConfirmedRecyclerAdapter (private val items: MutableList<ReservationListItem>) : BaseAdapter(){
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): ReservationListItem {
        return items[position]
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView

        if(view == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.reservation_list_item_confirmed, parent, false)
        }
        val item: ReservationListItem = items[position]

        val designerName = view?.findViewById<TextView>(R.id.reservation_name)
        designerName?.text = item.designerName

//        val designerProfile = view?.findViewById<ImageView>(R.id.)

//        val designer




        return view
    }
}