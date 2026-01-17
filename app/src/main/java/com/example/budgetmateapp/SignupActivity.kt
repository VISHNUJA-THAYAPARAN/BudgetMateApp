package com.example.budgetmateapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class SignupActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var phone: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var signupBtn: Button
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        username = findViewById(R.id.etUsername)
        phone = findViewById(R.id.etPhone)
        password = findViewById(R.id.etPassword)
        confirmPassword = findViewById(R.id.etConfirmPassword)
        signupBtn = findViewById(R.id.btnSignup)
        loginBtn = findViewById(R.id.btnLogin)

        createNotificationChannel()

        requestNotificationPermission()

        signupBtn.setOnClickListener {
            if (validateInput()) {
                saveData()
            }
        }

        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "budgetmate_channel",
                "Validation Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    private fun validateInput(): Boolean {
        val user = username.text.toString().trim()
        val phn = phone.text.toString().trim()
        val pass = password.text.toString().trim()
        val cpass = confirmPassword.text.toString().trim()

        if (!user.matches(Regex("^[A-Za-z0-9_]+$")) || user.contains(" ")) {
            showAlert("Username must not have spaces or special symbols except underscore (_)!")
            return false
        }

        if (!phn.matches(Regex("^(077|076|075|071|070|078)[0-9]{7}$"))) {
            showAlert("Phone must be 10 digits and start with 077,076,075,071,070,078")
            return false
        }

        val simplePatterns = listOf("123456", "abcdef", "ABCDEF")

        if (pass.length < 6 || simplePatterns.contains(pass)) {
            showAlert("Password too weak! Avoid simple patterns like 123456")
            return false
        }

        if (!pass.matches(Regex(".*[0-9!@#\$%^&*()].*"))) {
            showAlert("Password must include at least 1 number or symbol!")
            return false
        }

        if (pass != cpass) {
            showAlert("Passwords do not match!")
            return false
        }

        return true
    }

    private fun showAlert(message: String) {
        val builder = NotificationCompat.Builder(this, "budgetmate_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Input Validation Alert")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            NotificationManagerCompat.from(this)
                .notify((0..1000).random(), builder.build())
        }
    }




    private fun saveData() {
        val user = username.text.toString().trim()
        val phn = phone.text.toString().trim()
        val pass = password.text.toString().trim()

        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("username", user)
        editor.putString("phone", phn)
        editor.putString("password", pass)
        editor.apply()

        showAlert("Signup Successful!")

        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
