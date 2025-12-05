package com.example.kvalik_hotel

import android.view.LayoutInflater
import android.view.View
import com.example.kvalik_hotel.R
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoomAdapter(
    private val rooms: List<Room>,
    private val availabilityMap: Map<Int, Boolean>,
    private val onItemClick: (Room) -> Unit
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivRoomImage: ImageView = itemView.findViewById(R.id.ivRoomImage)
        val tvRoomNumber: TextView = itemView.findViewById(R.id.tvRoomNumber)
        val tvRoomType: TextView = itemView.findViewById(R.id.tvRoomType)
        val tvRoomCapacity: TextView = itemView.findViewById(R.id.tvRoomCapacity)
        val tvRoomPrice: TextView = itemView.findViewById(R.id.tvRoomPrice)
        val tvAvailability: TextView = itemView.findViewById(R.id.tvAvailability)

        fun bind(room: Room) {
            tvRoomNumber.text = "Номер ${room.roomNumber}"
            tvRoomType.text = room.roomType
            tvRoomCapacity.text = "${room.capacity} ${getPersonWord(room.capacity)}"
            tvRoomPrice.text = "${room.pricePerNight.toInt()} ₽/ночь"

            val isAvailable = availabilityMap[room.id] ?: true
            if (isAvailable) {
                tvAvailability.text = "Доступен"
                tvAvailability.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
            } else {
                tvAvailability.text = "Занят"
                tvAvailability.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
            }

            // Загрузка изображения номера
            val imageResId = itemView.context.resources.getIdentifier(
                room.imagePath, "drawable", itemView.context.packageName
            )
            if (imageResId != 0) {
                ivRoomImage.setImageResource(imageResId)
            } else {
                // Если изображение не найдено, показываем placeholder
                ivRoomImage.setImageResource(R.drawable.placeholder_room)
            }

            itemView.setOnClickListener {
                onItemClick(room)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    override fun getItemCount(): Int = rooms.size
}
