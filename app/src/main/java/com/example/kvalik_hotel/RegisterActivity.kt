package com.example.kvalik_hotel

import android.os.Bundle
import android.widget.Button
import com.example.kvalik_hotel.R
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etRegLogin: EditText
    private lateinit var etRegPassword: EditText
    private lateinit var etLastName: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etMiddleName: EditText
    private lateinit var btnRegisterSubmit: Button
    private lateinit var btnBackToLogin: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        etRegLogin = findViewById(R.id.etRegLogin)
        etRegPassword = findViewById(R.id.etRegPassword)
        etLastName = findViewById(R.id.etLastName)
        etFirstName = findViewById(R.id.etFirstName)
        etMiddleName = findViewById(R.id.etMiddleName)
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit)
        btnBackToLogin = findViewById(R.id.btnBackToLogin)

        btnRegisterSubmit.setOnClickListener {
            val login = etRegLogin.text.toString().trim()
            val password = etRegPassword.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val firstName = etFirstName.text.toString().trim()
            val middleName = etMiddleName.text.toString().trim()

            if (login.isEmpty() || password.isEmpty() || lastName.isEmpty() || 
                firstName.isEmpty() || middleName.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.registerUser(login, password, firstName, lastName, middleName)
            if (result != -1L) {
                Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Ошибка регистрации. Возможно, логин уже занят", Toast.LENGTH_SHORT).show()
            }
        }

        btnBackToLogin.setOnClickListener {
            finish()
        }
    }
}
