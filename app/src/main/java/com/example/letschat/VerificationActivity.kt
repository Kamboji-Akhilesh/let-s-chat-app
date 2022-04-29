package com.example.letschat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.letschat.databinding.ActivityVerificationBinding
import com.google.firebase.auth.FirebaseAuth

class VerificationActivity : AppCompatActivity() {

    var binding :ActivityVerificationBinding? = null

    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        auth = FirebaseAuth.getInstance()
        if (auth!!.currentUser!=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        supportActionBar?.hide()
        binding!!.editnumber.requestFocus()
        binding!!.ctnbtn.setOnClickListener {
            val intent = Intent(this,OtpActivity::class.java)
            intent.putExtra("Phone Number",binding!!.editnumber.text.toString())
            startActivity(intent)
        }

    }
}