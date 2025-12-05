package com.example.kvalik_hotel

import android.content.Intent
import android.os.Bundle
import com.example.kvalik_hotel.R
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AllRoomsActivity : AppCompatActivity() {

    private lateinit var rvAllRooms: RecyclerView
    private lateinit var btnFilterAll: Button
    private lateinit var btnFilterAvailable: Button
    private lateinit var btnFilterUnavailable: Button
    private lateinit var btnBackFromAllRooms: Button
    private lateinit var dbHelper: DatabaseHelper

    private var allRooms: List<Room> = listOf()
    private var availabilityMap: Map<Int, Boolean> = mapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_rooms)

        dbHelper = DatabaseHelper(this)

        rvAllRooms = findViewById(R.id.rvAllRooms)
        btnFilterAll = findViewById(R.id.btnFilterAll)
        btnFilterAvailable = findViewById(R.id.btnFilterAvailable)
        btnFilterUnavailable = findViewById(R.id.btnFilterUnavailable)
        btnBackFromAllRooms = findViewById(R.id.btnBackFromAllRooms)

        rvAllRooms.layoutManager = LinearLayoutManager(this)

        loadAllRooms()
        displayRooms(allRooms)

        btnFilterAll.setOnClickListener {
            displayRooms(allRooms)
        }

        btnFilterAvailable.setOnClickListener {
            val availableRooms = allRooms.filter { availabilityMap[it.id] == true }
            displayRooms(availableRooms)
        }

        btnFilterUnavailable.setOnClickListener {
            val unavailableRooms = allRooms.filter { availabilityMap[it.id] == false }
            displayRooms(unavailableRooms)
        }

        btnBackFromAllRooms.setOnClickListener {
            finish()
        }
    }

    private fun loadAllRooms() {
        allRooms = dbHelper.getAllRooms()
        availabilityMap = allRooms.associate { room ->
            room.id to dbHelper.isRoomAvailable(room.id)
        }
    }

    private fun displayRooms(rooms: List<Room>) {
        val adapter = RoomAdapter(rooms, availabilityMap) { room ->
            val intent = Intent(this, RoomDetailsActivity::class.java)
            intent.putExtra("ROOM_ID", room.id)
            startActivity(intent)
        }
        rvAllRooms.adapter = adapter
    }
}
