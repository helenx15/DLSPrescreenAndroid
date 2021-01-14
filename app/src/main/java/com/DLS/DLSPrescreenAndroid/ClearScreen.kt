package com.DLS.DLSPrescreenAndroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.clearscreen.*
import java.text.DateFormat
import java.util.*

class ClearScreen: AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clearscreen)

        // Firebase initialize
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        // Get current date
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)

        // Load saved information from questionnaire
        val prescreenPref = getSharedPreferences("PRESCREEN_INFO",Context.MODE_PRIVATE)

        clearNameLabel.text = currentUser?.displayName
        clearDateLabel.text = currentDate
        clearTimeLabel.text = prescreenPref.getString("TimeSubmitted", "")

        // Resubmit form button pressed; go to questionnaire and clear all saved information
        resubmitFormButton.setOnClickListener{
            val editor = prescreenPref.edit()
            editor.clear()
            editor.apply()
            startActivity(Intent(this, QuestionScreen:: class.java))
            this.finish()
        }
    }

}