package com.DLS.DLSPrescreenAndroid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
//import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

import kotlinx.android.synthetic.main.signupscreen.*

class SignUpScreen : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signupscreen)

        // User clicks "Sign Up" button
        signUpButtonMain.setOnClickListener {
            val name = signUpNameInput.text.toString().trim()
            val email = signUpEmailInput.text.toString().trim()
            val password = signUpPasswordInput.text.toString()

            // Checks if any fields are empty
            if (name == null || email == null || password == null || name == "" || email == "" || password == "") {
                signUpTextField.text = "Please fill out all entries."
                signUpTextField.isVisible = true
            }
            // Checks if password length is less than 6 characters
            else if (password.length < 6){
                signUpTextField.text = "Your password needs to be at least 6 characters."
                signUpTextField.isVisible = true
            }
            // Checks is email entered is a DLS email
            else if (!email.takeLast(10).equals("@dlshs.org") ) {
                signUpTextField.text = "Please sign up with a De La Salle email."
                signUpTextField.isVisible = true
            }
            // Attempt to create account
            else {

                signUpTextField.text = "Please wait while we create your account..."
                signUpTextField.isVisible = true

                // Initialize Firebase
                mAuth = FirebaseAuth.getInstance()

                // Attempt to create account with entries
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's name entry
                        val user = mAuth.currentUser
                        val profileUpdates =
                            UserProfileChangeRequest.Builder().setDisplayName(name).build()
                        user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                            } else {
                                signUpTextField.text = "Error: Unable to save name."
                            }
                        }

                        // Send user a verification email
                        user!!.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    signUpTextField.text =
                                        "An email has been sent to your email. Please verify your account then log in."
                                    signUpLoginButton.isVisible = true
                                } else {
                                    signUpTextField.text =
                                        "Unable to send verification email. Try again, or try a different email."
                                }
                            }

                    // Unable to create an account
                    } else {
                        signUpTextField.text =
                            "Error: User already exists or one or more entries are invalid. Try again, or log in."
                        signUpLoginButton.isVisible = true
                    }
                }
            }
        }

        // Log in button is pressed; go to log in screen
        signUpLoginButton.setOnClickListener {
            val intent = Intent(this, LogInScreen:: class.java)
            startActivity(intent)
            this.finish();
        }
    }


}