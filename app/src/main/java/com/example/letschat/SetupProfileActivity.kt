package com.example.letschat

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.letschat.databinding.ActivitySetupProfileBinding
import com.example.letschat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class SetupProfileActivity : AppCompatActivity() {

    private var binding: ActivitySetupProfileBinding? = null
    private var auth: FirebaseAuth? = null
    private var database: FirebaseDatabase? = null
    private var storage: FirebaseStorage? = null
    private var selectedImage: Uri? = null
    private var dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        dialog = ProgressDialog(this)
        dialog!!.setMessage("Updating profile...")
        dialog!!.setCancelable(false)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        binding!!.imageView.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 45)
        })
        binding!!.ctnbtn2.setOnClickListener(View.OnClickListener {
            val name: String = binding!!.editname.text.toString()
            if (name.isEmpty()) {
                binding!!.editname.error = "Please type a name"
                return@OnClickListener
            }
            dialog!!.show()
            if (selectedImage != null) {
                val reference = storage!!.reference.child("Profiles").child(
                    auth!!.uid!!
                )
                reference.putFile(selectedImage!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reference.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            val uid = auth!!.uid
                            val phone = auth!!.currentUser!!.phoneNumber
                            val name: String = binding!!.editname.text.toString()
                            val user = User(uid, name, phone, imageUrl)
                            database!!.reference
                                .child("users")
                                .child(uid!!)
                                .setValue(user)
                                .addOnSuccessListener {
                                    dialog!!.dismiss()
                                    val intent = Intent(
                                        this@SetupProfileActivity,
                                        MainActivity::class.java
                                    )
                                    startActivity(intent)
                                    finish()
                                }
                        }
                    }
                }
            } else {
                val uid = auth!!.uid
                val phone = auth!!.currentUser!!.phoneNumber
                val user = User(uid, name, phone, "No Image")
                database!!.reference
                    .child("users")
                    .child(uid!!)
                    .setValue(user)
                    .addOnSuccessListener {
                        dialog!!.dismiss()
                        val intent = Intent(this@SetupProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (data.data != null) {
                val uri = data.data // filepath
                val storage = FirebaseStorage.getInstance()
                val time = Date().time
                val reference = storage.reference.child("Profiles").child(time.toString() + "")
                reference.putFile(uri!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reference.downloadUrl.addOnSuccessListener { uri ->
                            val filePath = uri.toString()
                            val obj = HashMap<String, Any>()
                            obj["image"] = filePath
                            database!!.reference.child("users")
                                .child(FirebaseAuth.getInstance().uid!!)
                                .updateChildren(obj).addOnSuccessListener { }
                        }
                    }
                }
                binding!!.imageView.setImageURI(data.data)
                selectedImage = data.data
            }
        }
    }
}
