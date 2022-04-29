package com.example.letschat.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letschat.R
import com.example.letschat.databinding.DeleteLayoutBinding
import com.example.letschat.databinding.RecieveMsgBinding
import com.example.letschat.databinding.SendMsgBinding
import com.example.letschat.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MessagesAdapter(
    var context: Context,
    messages: ArrayList<Message>?,
    senderRoom: String,
    receiverRoom: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private lateinit var messages: ArrayList<Message>
    private val sent = 1
    private val received = 2
    private var senderRoom: String
    private var receiverRoom: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == sent) {
            val view= LayoutInflater.from(context).inflate(R.layout.send_msg, parent, false)
            SentMsgHolder(view)
        } else {
            val view1= LayoutInflater.from(context).inflate(R.layout.recieve_msg, parent, false)
            ReceiveMsgHolder(view1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message: Message = messages[position]
        return if (FirebaseAuth.getInstance().uid == message.senderId) {
            sent
        } else {
            received
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder.javaClass == SentMsgHolder::class.java) {
            val viewHolder = holder as SentMsgHolder
            if (message.message.equals("photo")) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mlinear.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageurl)
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.binding.image)
            }
            viewHolder.binding.message.text = message.message
            viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog: AlertDialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()
                binding.everyone.setOnClickListener(View.OnClickListener {
                    message.message = "This message is removed."
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(message)
                    }
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(it1).setValue(message)
                    }
                    dialog.dismiss()
                })
                binding.delete.setOnClickListener(View.OnClickListener {
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(null)
                    }
                    dialog.dismiss()
                })
                binding.cancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
                dialog.show()
                false
            }
        } else {
            val viewHolder = holder as ReceiveMsgHolder
            if (message.message.equals("photo")) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.msg1.visibility = View.GONE
                viewHolder.binding.linear.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageurl)
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.binding.image)
            }
            viewHolder.binding.msg1.text = message.message
            viewHolder.itemView.setOnLongClickListener {
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog: AlertDialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()
                binding.everyone.setOnClickListener {
                    message.message = "This message is removed."
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(message)
                    }
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(it1).setValue(message)
                    }
                    dialog.dismiss()
                }
                binding.delete.setOnClickListener(View.OnClickListener {
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(null)
                    }
                    dialog.dismiss()
                })
                binding.cancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
                dialog.show()
                false

            }

        }
    }

    override fun getItemCount(): Int = messages.size

    inner class SentMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SendMsgBinding = SendMsgBinding.bind(itemView)
    }

    inner class ReceiveMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RecieveMsgBinding = RecieveMsgBinding.bind(itemView)
    }

    init {
        if (messages != null) {
            this.messages = messages
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }
}