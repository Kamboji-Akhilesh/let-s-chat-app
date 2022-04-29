package com.example.letschat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letschat.ChatActivity
import com.example.letschat.R
import com.example.letschat.databinding.ItemProfileBinding
import com.example.letschat.model.User

class UserAdapter(var context:Context,var userList:ArrayList<User>):
RecyclerView.Adapter<UserAdapter.UserViewHolder>()
{
    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding : ItemProfileBinding = ItemProfileBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_profile,parent,false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.binding.username.text = user.name
        Glide.with(context).load(user.profileimage)
            .placeholder(R.drawable.ic_round_account_circle_24)
            .into(holder.binding.profile)
        holder.itemView.setOnClickListener{
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("name",user.name)
            intent.putExtra("image",user.profileimage)
            intent.putExtra("uid",user.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = userList.size
}