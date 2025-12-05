package com.example.kvalik_hotel

import android.os.Bundle
import android.widget.Button
import com.example.kvalik_hotel.R
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RoomDetailsActivity : AppCompatActivity() {

    private lateinit var ivRoomDetailImage: ImageView
    private lateinit var tvDetailRoomNumber: TextView
    private lateinit var tvDetailRoomType: TextView
    private lateinit var tvDetailCapacity: TextView
    private lateinit var tvDetailPrice: TextView
    private lateinit var tvDetailDescription: TextView
    private lateinit var tvDetailAvailability: TextView
    private lateinit var tvDetailAmenities: TextView
    private lateinit var btnBackFromDetails: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_details)

        dbHelper = DatabaseHelper(this)

        ivRoomDetailImage = findViewById(R.id.ivRoomDetailImage)
        tvDetailRoomNumber = findViewById(R.id.tvDetailRoomNumber)
        tvDetailRoomType = findViewById(R.id.tvDetailRoomType)
        tvDetailCapacity = findViewById(R.id.tvDetailCapacity)
        tvDetailPrice = findViewById(R.id.tvDetailPrice)
        tvDetailDescription = findViewById(R.id.tvDetailDescription)
        tvDetailAvailability = findViewById(R.id.tvDetailAvailability)
        tvDetailAmenities = findViewById(R.id.tvDetailAmenities)
        btnBackFromDetails = findViewById(R.id.btnBackFromDetails)

        val roomId = intent.getIntExtra("ROOM_ID", -1)
        if (roomId != -1) {
            val room = dbHelper.getRoomById(roomId)
            if (room != null) {
                displayRoomDetails(room)
            }
        }

        btnBackFromDetails.setOnClickListener {
            finish()
        }
    }

    private fun displayRoomDetails(room: Room) {
        tvDetailRoomNumber.text = "Номер ${room.roomNumber}"
        tvDetailRoomType.text = room.roomType
        tvDetailCapacity.text = "${room.capacity} ${getPersonWord(room.capacity)}"
        tvDetailPrice.text = "${room.pricePerNight.toInt()} ₽"
        tvDetailDescription.text = room.description

        val isAvailable = dbHelper.isRoomAvailable(room.id)
        if (isAvailable) {
            tvDetailAvailability.text = "Статус: Доступен"
            tvDetailAvailability.setTextColor(getColor(android.R.color.holo_green_dark))
        } else {
            tvDetailAvailability.text = "Статус: Занят"
            tvDetailAvailability.setTextColor(getColor(android.R.color.holo_red_dark))
        }

        // Отображение удобств
        val amenities = mutableListOf<String>()
        if (room.hasWifi) amenities.add("Wi-Fi")
        if (room.hasTV) amenities.add("Телевизор")
        if (room.hasAirConditioning) amenities.add("Кондиционер")
        if (room.hasMinibar) amenities.add("Минибар")
        if (room.hasBalcony) amenities.add("Балкон")
        
        tvDetailAmenities.text = if (amenities.isNotEmpty()) {
            amenities.joinToString(" • ")
        } else {
            "Нет дополнительных удобств"
        }

        // Загрузка изображения номера
        val imageResId = resources.getIdentifier(
            room.imagePath, "drawable", packageName
        )
        if (imageResId != 0) {
            ivRoomDetailImage.setImageResource(imageResId)
        } else {
            // Если изображение не найдено, показываем placeholder
            ivRoomDetailImage.setImageResource(R.drawable.placeholder_room)
        }
    }

    private fun getPersonWord(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "человек"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "человека"
            else -> "человек"
        }
    }
}
