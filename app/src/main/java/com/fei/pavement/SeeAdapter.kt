package com.sunnyweather.fsystem.activity.ui.dashboard

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.fsystem.R
import com.sunnyweather.fsystem.activity.ui.dashboard.otherdepartment.OtherDepartment
import com.sunnyweather.fsystem.model.DeptMember
import com.sunnyweather.fsystem.model.Member


class MinisterRecyclerviewAdapter(val list: ArrayList<DeptMember>) :
    RecyclerView.Adapter<MinisterRecyclerviewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val memberImageView:ImageView=view.findViewById(R.id.member_imageView)
        val name: TextView = view.findViewById(R.id.member_text_name)
        val position: TextView = view.findViewById(R.id.member_text_position)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member ,parent, false)

        val viewHolder= ViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = list[position]
        holder.name.text=friend.username
        holder.position.text= friend.roleName
        if(friend.roleName=="部长"){
            holder.memberImageView.setImageResource(R.drawable.ic_chengyuan2)
        }else{
            holder.memberImageView.setImageResource(R.drawable.ic_chengyuan)
        }

    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
}