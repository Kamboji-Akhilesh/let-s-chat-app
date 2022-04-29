package com.example.letschat

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.letschat.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    var binding : ActivityOtpBinding? = null
    private var auth : FirebaseAuth? = null
    var verificationId : String? = null
    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        dialog = ProgressDialog(this)
        dialog!!.setMessage("Sending OTP...")
        dialog!!.setCancelable(false)
        dialog!!.show()
        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        val phoneNumber = intent.getStringExtra("Phone Number")
        binding!!.phonelbl.text = "Verify $phoneNumber"

        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    TODO()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    TODO()
                }

                override fun onCodeSent(
                    verifyId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verifyId,forceResendingToken)
                    dialog!!.dismiss()
                    verificationId = verifyId
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
                    binding!!.otpView.requestFocus()
                }

            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        binding!!.otpView.setOtpCompletionListener {otp->
            val credential = PhoneAuthProvider.getCredential(verificationId!!,otp)
            auth!!.signInWithCredential(credential)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        val intent = Intent(this,SetupProfileActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    } else{
                        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}