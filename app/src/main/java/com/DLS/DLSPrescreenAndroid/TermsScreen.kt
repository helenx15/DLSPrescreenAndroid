package com.DLS.DLSPrescreenAndroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.termsscreen.*

class TermsScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.termsscreen)

        acceptButton.setOnClickListener {
            // Set accepted terms to true
            val termsPref = getSharedPreferences("TERMS_INFO", Context.MODE_PRIVATE)
            val termseditor = termsPref.edit()
            val acceptedterms = termsPref.getString("AcceptedTerms", "")
            if (acceptedterms == "") {
                termseditor.putString("AcceptedTerms", "Yes")
                termseditor.apply()
                startActivity(Intent(this, MainActivity:: class.java))
                this.finish();
            }
        }

        declineButton.setOnClickListener {
            termsErrorLabel.isVisible = true
        }
    }
}