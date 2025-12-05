package com.example.kvalik_hotel

import android.content.Intent
import android.os.Bundle
import com.example.kvalik_hotel.R
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var tvUserName: TextView
    private lateinit var btnSearchRooms: Button
    private lateinit var btnAllRooms: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        tvWelcome = findViewById(R.id.tvWelcome)
        tvUserName = findViewById(R.id.tvUserName)
        btnSearchRooms = findViewById(R.id.btnSearchRooms)
        btnAllRooms = findViewById(R.id.btnAllRooms)
        btnLogout = findViewById(R.id.btnLogout)

        val userName = intent.getStringExtra("USER_NAME") ?: "Гость"
        tvUserName.text = userName

        btnSearchRooms.setOnClickListener {
            val intent = Intent(this, SearchFiltersActivity::class.java)
            startActivity(intent)
        }

        btnAllRooms.setOnClickListener {
            val intent = Intent(this, AllRoomsActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            finish()
        }
    }
}
