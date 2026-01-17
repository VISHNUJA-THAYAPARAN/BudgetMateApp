package com.example.budgetmateapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameInput = findViewById(R.id.etLoginUsername)
        passwordInput = findViewById(R.id.etLoginPassword)
        loginBtn = findViewById(R.id.btnLoginNow)
        signupBtn = findViewById(R.id.btnGoToSignup)

        loginBtn.setOnClickListener {
            validateLogin()
        }

        signupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun validateLogin() {
        val enteredUser = usernameInput.text.toString().trim()
        val enteredPass = passwordInput.text.toString().trim()

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val savedUser = sharedPref.getString("username", null)
        val savedPass = sharedPref.getString("password", null)

        if (enteredUser == savedUser && enteredPass == savedPass) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
        }
    }
}
