package com.DLS.DLSPrescreenAndroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
//import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.infoscreen.*

class InfoScreen: AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.infoscreen)

        // Firebase loading info from email
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        if (currentUser?.displayName == currentUser?.email) {
            infoNameTitleLabel.isVisible = false
            infoNameLabel.isVisible = false
        }

        infoNameLabel.text = currentUser?.displayName
        infoEmailLabel.text = currentUser?.email

        // Sign out button pressed, user is signed out
        signOutButton.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, MainActivity:: class.java)
            startActivity(intent)
            this.finish();
        }

        // Next button pressed; go to prescreen
        infoNextButton.setOnClickListener{
            val intent = (Intent(this, QuestionScreen:: class.java))
            startActivity(intent)
            this.finish();

        }

    }

}