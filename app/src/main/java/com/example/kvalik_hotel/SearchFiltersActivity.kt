package com.example.kvalik_hotel

import android.app.DatePickerDialog
import android.content.Intent
import com.example.kvalik_hotel.R
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class SearchFiltersActivity : AppCompatActivity() {

    private lateinit var etCheckInDate: EditText
    private lateinit var etCheckOutDate: EditText
    private lateinit var etGuestCount: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnBack: Button
    private lateinit var dbHelper: DatabaseHelper

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_filters)

        dbHelper = DatabaseHelper(this)

        etCheckInDate = findViewById(R.id.etCheckInDate)
        etCheckOutDate = findViewById(R.id.etCheckOutDate)
        etGuestCount = findViewById(R.id.etGuestCount)
        btnSearch = findViewById(R.id.btnSearch)
        btnBack = findViewById(R.id.btnBack)

        etCheckInDate.setOnClickListener {
            showDatePicker { date ->
                etCheckInDate.setText(date)
            }
        }

        etCheckOutDate.setOnClickListener {
            showDatePicker { date ->
                etCheckOutDate.setText(date)
            }
        }

        btnSearch.setOnClickListener {
            val checkIn = etCheckInDate.text.toString().trim()
            val checkOut = etCheckOutDate.text.toString().trim()
            val guestCountStr = etGuestCount.text.toString().trim()

            if (checkIn.isEmpty() || checkOut.isEmpty() || guestCountStr.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val guestCount = guestCountStr.toIntOrNull()
            if (guestCount == null || guestCount <= 0) {
                Toast.makeText(this, "Введите корректное количество гостей", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, RoomListActivity::class.java)
            intent.putExtra("CHECK_IN", checkIn)
            intent.putExtra("CHECK_OUT", checkOut)
            intent.putExtra("GUEST_COUNT", guestCount)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                onDateSelected(dateFormat.format(calendar.time))
            },
            year, month, day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}
