package com.example.kvalik_hotel

import android.content.Intent
import android.os.Bundle
import com.example.kvalik_hotel.R
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RoomListActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var rvRooms: RecyclerView
    private lateinit var btnBackFromList: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_list)

        dbHelper = DatabaseHelper(this)

        tvTitle = findViewById(R.id.tvTitle)
        rvRooms = findViewById(R.id.rvRooms)
        btnBackFromList = findViewById(R.id.btnBackFromList)

        rvRooms.layoutManager = LinearLayoutManager(this)

        val checkIn = intent.getStringExtra("CHECK_IN") ?: ""
        val checkOut = intent.getStringExtra("CHECK_OUT") ?: ""
        val guestCount = intent.getIntExtra("GUEST_COUNT", 1)

        val rooms = dbHelper.getAvailableRooms(checkIn, checkOut, guestCount)
        
        if (rooms.isEmpty()) {
            tvTitle.text = "Номера не найдены"
        } else {
            tvTitle.text = "Найдено номеров: ${rooms.size}"
        }

        // Все номера из фильтра доступны
        val availabilityMap = rooms.associate { it.id to true }

        val adapter = RoomAdapter(rooms, availabilityMap) { room ->
            val intent = Intent(this, RoomDetailsActivity::class.java)
            intent.putExtra("ROOM_ID", room.id)
            startActivity(intent)
        }

        rvRooms.adapter = adapter

        btnBackFromList.setOnClickListener {
            finish()
        }
    }
}
