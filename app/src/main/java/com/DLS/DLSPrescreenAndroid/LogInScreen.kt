package com.DLS.DLSPrescreenAndroid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
//import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.loginscreen.*


class LogInScreen: AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginscreen)

        // Firebase initialize
        mAuth = FirebaseAuth.getInstance()

        // User clicks "Log In" button
        loginButton.setOnClickListener {
            val email = loginEmailInput.text.toString().trim()
            val password = loginPasswordInput.text.toString()

            // Checks if any fields are empty
            if (email == null || password == null || email == "" || password == "") {
                loginTextField.text = "Please fill out all entries."
                loginTextField.isVisible = true
            }
            // Attempt to log in
            else {
                loginTextField.text = "Logging you in..."
                loginTextField.isVisible = true

                // Attempt to log in with entries
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser

                        // Checks if user is email verified
                        if (user?.isEmailVerified == true) {

                            if(user.email == null || user.email == "") {
                                user.updateEmail(email)
                            }
                            if(user.displayName == null || user.displayName == "") {
                                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(email).build()
                                user.updateProfile(profileUpdates)
                            }

                            // User is verified, log in
                            val intent = Intent(this, QuestionScreen::class.java)
                            startActivity(intent)
                            this.finish();
                        }
                        // User is not email verified
                        else {
                            loginTextField.text =
                                "Your email has not been verified. Another verification email has been sent."

                            // Send user a verification email
                            user!!.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        loginTextField.text =
                                            "An email has been sent to your email. Please verify your account then log in."
                                    } else {
                                        loginTextField.text =
                                            "Unable to send verification email."
                                    }
                                }
                        }
                    }
                    // Unable to log in
                    else {
                        loginTextField.text =
                            "Invalid email and password credentials. Try again, or:"
                        loginSignUpButton.isVisible = true
                        resetPasswordButton.isVisible = true
                    }
                }
            }
        }

        // Sign up button is pressed; go to sign up screen
        loginSignUpButton.setOnClickListener {
            val intent = Intent(this, SignUpScreen:: class.java)
            startActivity(intent)
            this.finish();
        }

        // Reset password button is pressed
        resetPasswordButton.setOnClickListener {
            val email = loginEmailInput.text.toString().trim()

            // Send a reset email to the email entered
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    loginTextField.text = "Password reset has been sent to $email."
                } else {
                    loginTextField.text = "No user exists. Please create an account."
                    resetPasswordButton.isVisible = false
                }
            }
        }
    }

}