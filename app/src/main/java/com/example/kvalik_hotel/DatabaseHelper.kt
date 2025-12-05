package com.example.kvalik_hotel

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "hotel.db"
        // Повысим версию БД, чтобы при обновлении приложения тестовые брони перечитались и
        // вставились в таблицу
        private const val DATABASE_VERSION = 4

        // Таблица пользователей
        private const val TABLE_USERS = "users"
        private const val COL_USER_ID = "id"
        private const val COL_USER_LOGIN = "login"
        private const val COL_USER_PASSWORD = "password"
        private const val COL_USER_FIRST_NAME = "first_name"
        private const val COL_USER_LAST_NAME = "last_name"
        private const val COL_USER_MIDDLE_NAME = "middle_name"

        // Таблица номеров
        private const val TABLE_ROOMS = "rooms"
        private const val COL_ROOM_ID = "id"
        private const val COL_ROOM_NUMBER = "room_number"
        private const val COL_ROOM_TYPE = "room_type"
        private const val COL_ROOM_CAPACITY = "capacity"
        private const val COL_ROOM_PRICE = "price_per_night"
        private const val COL_ROOM_DESCRIPTION = "description"
        private const val COL_ROOM_IMAGE = "image_path"
        private const val COL_ROOM_HAS_WIFI = "has_wifi"
        private const val COL_ROOM_HAS_TV = "has_tv"
        private const val COL_ROOM_HAS_AC = "has_air_conditioning"
        private const val COL_ROOM_HAS_MINIBAR = "has_minibar"
        private const val COL_ROOM_HAS_BALCONY = "has_balcony"

        // Таблица бронирований
        private const val TABLE_BOOKINGS = "bookings"
        private const val COL_BOOKING_ID = "id"
        private const val COL_BOOKING_ROOM_ID = "room_id"
        private const val COL_BOOKING_CHECK_IN = "check_in_date"
        private const val COL_BOOKING_CHECK_OUT = "check_out_date"
        private const val COL_BOOKING_GUEST_COUNT = "guest_count"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Создание таблицы пользователей
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_LOGIN TEXT NOT NULL UNIQUE,
                $COL_USER_PASSWORD TEXT NOT NULL,
                $COL_USER_FIRST_NAME TEXT NOT NULL,
                $COL_USER_LAST_NAME TEXT NOT NULL,
                $COL_USER_MIDDLE_NAME TEXT NOT NULL
            )
        """.trimIndent()

        // Создание таблицы номеров
        val createRoomsTable = """
            CREATE TABLE $TABLE_ROOMS (
                $COL_ROOM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ROOM_NUMBER TEXT NOT NULL UNIQUE,
                $COL_ROOM_TYPE TEXT NOT NULL,
                $COL_ROOM_CAPACITY INTEGER NOT NULL,
                $COL_ROOM_PRICE REAL NOT NULL,
                $COL_ROOM_DESCRIPTION TEXT NOT NULL,
                $COL_ROOM_IMAGE TEXT NOT NULL,
                $COL_ROOM_HAS_WIFI INTEGER DEFAULT 1,
                $COL_ROOM_HAS_TV INTEGER DEFAULT 1,
                $COL_ROOM_HAS_AC INTEGER DEFAULT 1,
                $COL_ROOM_HAS_MINIBAR INTEGER DEFAULT 0,
                $COL_ROOM_HAS_BALCONY INTEGER DEFAULT 0
            )
        """.trimIndent()

        // Создание таблицы бронирований
        val createBookingsTable = """
            CREATE TABLE $TABLE_BOOKINGS (
                $COL_BOOKING_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_BOOKING_ROOM_ID INTEGER NOT NULL,
                $COL_BOOKING_CHECK_IN TEXT NOT NULL,
                $COL_BOOKING_CHECK_OUT TEXT NOT NULL,
                $COL_BOOKING_GUEST_COUNT INTEGER NOT NULL,
                FOREIGN KEY ($COL_BOOKING_ROOM_ID) REFERENCES $TABLE_ROOMS($COL_ROOM_ID)
            )
        """.trimIndent()

        db?.execSQL(createUsersTable)
        db?.execSQL(createRoomsTable)
        db?.execSQL(createBookingsTable)

        // Заполнение тестовыми данными
        insertTestData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ROOMS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKINGS")
        onCreate(db)
    }

    private fun insertTestData(db: SQLiteDatabase?) {
        // Добавление тестовых пользователей
        val user1 = ContentValues().apply {
            put(COL_USER_LOGIN, "1")
            put(COL_USER_PASSWORD, "1")
            put(COL_USER_FIRST_NAME, "Иван")
            put(COL_USER_LAST_NAME, "Иванов")
            put(COL_USER_MIDDLE_NAME, "Иванович")
        }

        val user2 = ContentValues().apply {
            put(COL_USER_LOGIN, "2")
            put(COL_USER_PASSWORD, "2")
            put(COL_USER_FIRST_NAME, "Петр")
            put(COL_USER_LAST_NAME, "Петров")
            put(COL_USER_MIDDLE_NAME, "Петрович")
        }

        db?.insert(TABLE_USERS, null, user1)
        db?.insert(TABLE_USERS, null, user2)

        // Добавление тестовых номеров
        val rooms = listOf(
            ContentValues().apply {
                put(COL_ROOM_NUMBER, "101")
                put(COL_ROOM_TYPE, "Стандарт")
                put(COL_ROOM_CAPACITY, 2)
                put(COL_ROOM_PRICE, 3000.0)
                put(COL_ROOM_DESCRIPTION, "Уютный стандартный номер с двуспальной кроватью")
                put(COL_ROOM_IMAGE, "room_101")
                put(COL_ROOM_HAS_WIFI, 1)
                put(COL_ROOM_HAS_TV, 1)
                put(COL_ROOM_HAS_AC, 1)
                put(COL_ROOM_HAS_MINIBAR, 0)
                put(COL_ROOM_HAS_BALCONY, 0)
            },
            ContentValues().apply {
                put(COL_ROOM_NUMBER, "102")
                put(COL_ROOM_TYPE, "Стандарт")
                put(COL_ROOM_CAPACITY, 2)
                put(COL_ROOM_PRICE, 3000.0)
                put(COL_ROOM_DESCRIPTION, "Комфортный номер с видом на город")
                put(COL_ROOM_IMAGE, "room_102")
                put(COL_ROOM_HAS_WIFI, 1)
                put(COL_ROOM_HAS_TV, 1)
                put(COL_ROOM_HAS_AC, 1)
                put(COL_ROOM_HAS_MINIBAR, 0)
                put(COL_ROOM_HAS_BALCONY, 1)
            },
            ContentValues().apply {
                put(COL_ROOM_NUMBER, "201")
                put(COL_ROOM_TYPE, "Люкс")
                put(COL_ROOM_CAPACITY, 3)
                put(COL_ROOM_PRICE, 5500.0)
                put(COL_ROOM_DESCRIPTION, "Просторный люкс с гостиной зоной и джакузи")
                put(COL_ROOM_IMAGE, "room_201")
                put(COL_ROOM_HAS_WIFI, 1)
                put(COL_ROOM_HAS_TV, 1)
                put(COL_ROOM_HAS_AC, 1)
                put(COL_ROOM_HAS_MINIBAR, 1)
                put(COL_ROOM_HAS_BALCONY, 1)
            },
            ContentValues().apply {
                put(COL_ROOM_NUMBER, "202")
                put(COL_ROOM_TYPE, "Люкс")
                put(COL_ROOM_CAPACITY, 3)
                put(COL_ROOM_PRICE, 5500.0)
                put(COL_ROOM_DESCRIPTION, "Роскошный номер с панорамным видом")
                put(COL_ROOM_IMAGE, "room_202")
                put(COL_ROOM_HAS_WIFI, 1)
                put(COL_ROOM_HAS_TV, 1)
                put(COL_ROOM_HAS_AC, 1)
                put(COL_ROOM_HAS_MINIBAR, 1)
                put(COL_ROOM_HAS_BALCONY, 1)
            },
            ContentValues().apply {
                put(COL_ROOM_NUMBER, "301")
                put(COL_ROOM_TYPE, "Семейный")
                put(COL_ROOM_CAPACITY, 4)
                put(COL_ROOM_PRICE, 6000.0)
                put(COL_ROOM_DESCRIPTION, "Большой семейный номер с двумя спальнями")
                put(COL_ROOM_IMAGE, "room_301")
                put(COL_ROOM_HAS_WIFI, 1)
                put(COL_ROOM_HAS_TV, 1)
                put(COL_ROOM_HAS_AC, 1)
                put(COL_ROOM_HAS_MINIBAR, 0)
                put(COL_ROOM_HAS_BALCONY, 1)
            },
            ContentValues().apply {
                put(COL_ROOM_NUMBER, "302")
                put(COL_ROOM_TYPE, "Семейный")
                put(COL_ROOM_CAPACITY, 4)
                put(COL_ROOM_PRICE, 6000.0)
                put(COL_ROOM_DESCRIPTION, "Идеально для семейного отдыха")
                put(COL_ROOM_IMAGE, "room_302")
                put(COL_ROOM_HAS_WIFI, 1)
                put(COL_ROOM_HAS_TV, 1)
                put(COL_ROOM_HAS_AC, 1)
                put(COL_ROOM_HAS_MINIBAR, 0)
                put(COL_ROOM_HAS_BALCONY, 1)
            },
            ContentValues().apply {
                put(COL_ROOM_NUMBER, "401")
                put(COL_ROOM_TYPE, "Президентский")
                put(COL_ROOM_CAPACITY, 5)
                put(COL_ROOM_PRICE, 10000.0)
                put(COL_ROOM_DESCRIPTION, "Роскошный президентский люкс с панорамными окнами")
                put(COL_ROOM_IMAGE, "room_401")
                put(COL_ROOM_HAS_WIFI, 1)
                put(COL_ROOM_HAS_TV, 1)
                put(COL_ROOM_HAS_AC, 1)
                put(COL_ROOM_HAS_MINIBAR, 1)
                put(COL_ROOM_HAS_BALCONY, 1)
            },
            ContentValues().apply {
                put(COL_ROOM_NUMBER, "103")
                put(COL_ROOM_TYPE, "Эконом")
                put(COL_ROOM_CAPACITY, 1)
                put(COL_ROOM_PRICE, 2000.0)
                put(COL_ROOM_DESCRIPTION, "Компактный номер для одного гостя")
                put(COL_ROOM_IMAGE, "room_103")
                put(COL_ROOM_HAS_WIFI, 1)
                put(COL_ROOM_HAS_TV, 0)
                put(COL_ROOM_HAS_AC, 0)
                put(COL_ROOM_HAS_MINIBAR, 0)
                put(COL_ROOM_HAS_BALCONY, 0)
            },
            ContentValues().apply {
                put(COL_ROOM_NUMBER, "104")
                put(COL_ROOM_TYPE, "Эконом")
                put(COL_ROOM_CAPACITY, 1)
                put(COL_ROOM_PRICE, 2000.0)
                put(COL_ROOM_DESCRIPTION, "Бюджетный вариант с удобствами")
                put(COL_ROOM_IMAGE, "room_104")
                put(COL_ROOM_HAS_WIFI, 1)
                put(COL_ROOM_HAS_TV, 0)
                put(COL_ROOM_HAS_AC, 0)
                put(COL_ROOM_HAS_MINIBAR, 0)
                put(COL_ROOM_HAS_BALCONY, 0)
            },
            ContentValues().apply {
                put(COL_ROOM_NUMBER, "203")
                put(COL_ROOM_TYPE, "Делюкс")
                put(COL_ROOM_CAPACITY, 2)
                put(COL_ROOM_PRICE, 4500.0)
                put(COL_ROOM_DESCRIPTION, "Элегантный номер с современным дизайном")
                put(COL_ROOM_IMAGE, "room_203")
                put(COL_ROOM_HAS_WIFI, 1)
                put(COL_ROOM_HAS_TV, 1)
                put(COL_ROOM_HAS_AC, 1)
                put(COL_ROOM_HAS_MINIBAR, 1)
                put(COL_ROOM_HAS_BALCONY, 0)
            }
        )

        rooms.forEach { db?.insert(TABLE_ROOMS, null, it) }

        // Добавление тестовых бронирований (некоторые номера будут заняты)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        
        // Бронирование номера 101 на ближайшие даты
        val booking1 = ContentValues().apply {
            put(COL_BOOKING_ROOM_ID, 1)
            put(COL_BOOKING_CHECK_IN, sdf.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 3)
            put(COL_BOOKING_CHECK_OUT, sdf.format(calendar.time))
            put(COL_BOOKING_GUEST_COUNT, 2)
        }

        // Бронирование номера 201
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_MONTH, 5)
        val booking2 = ContentValues().apply {
            put(COL_BOOKING_ROOM_ID, 3)
            put(COL_BOOKING_CHECK_IN, sdf.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 4)
            put(COL_BOOKING_CHECK_OUT, sdf.format(calendar.time))
            put(COL_BOOKING_GUEST_COUNT, 3)
        }

        // Бронирование номера 301
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_MONTH, 2)
        val booking3 = ContentValues().apply {
            put(COL_BOOKING_ROOM_ID, 5)
            put(COL_BOOKING_CHECK_IN, sdf.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 5)
            put(COL_BOOKING_CHECK_OUT, sdf.format(calendar.time))
            put(COL_BOOKING_GUEST_COUNT, 4)
        }

        db?.insert(TABLE_BOOKINGS, null, booking1)
        db?.insert(TABLE_BOOKINGS, null, booking2)
        db?.insert(TABLE_BOOKINGS, null, booking3)
    }

    // Методы для работы с пользователями
    fun checkLogin(login: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COL_USER_LOGIN = ? AND $COL_USER_PASSWORD = ?",
            arrayOf(login, password),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_LOGIN)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_MIDDLE_NAME))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    fun registerUser(login: String, password: String, firstName: String, lastName: String, middleName: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_USER_LOGIN, login)
            put(COL_USER_PASSWORD, password)
            put(COL_USER_FIRST_NAME, firstName)
            put(COL_USER_LAST_NAME, lastName)
            put(COL_USER_MIDDLE_NAME, middleName)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    // Методы для работы с номерами
    fun getAllRooms(): List<Room> {
        val rooms = mutableListOf<Room>()
        val db = readableDatabase
        val cursor = db.query(TABLE_ROOMS, null, null, null, null, null, COL_ROOM_NUMBER)

        while (cursor.moveToNext()) {
            rooms.add(cursorToRoom(cursor))
        }
        cursor.close()
        return rooms
    }

    fun getAvailableRooms(checkIn: String, checkOut: String, guestCount: Int): List<Room> {
        val rooms = mutableListOf<Room>()
        val db = readableDatabase

        // Находим все номера с нужной вместимостью
        val roomQuery = "SELECT * FROM $TABLE_ROOMS WHERE $COL_ROOM_CAPACITY >= ?"
        val roomCursor = db.rawQuery(roomQuery, arrayOf(guestCount.toString()))

        while (roomCursor.moveToNext()) {
            val roomId = roomCursor.getInt(roomCursor.getColumnIndexOrThrow(COL_ROOM_ID))
            
            // Проверяем, не забронирован ли номер на эти даты
            val bookingQuery = """
                SELECT COUNT(*) FROM $TABLE_BOOKINGS 
                WHERE $COL_BOOKING_ROOM_ID = ? 
                AND NOT ($COL_BOOKING_CHECK_OUT <= ? OR $COL_BOOKING_CHECK_IN >= ?)
            """.trimIndent()
            
            val bookingCursor = db.rawQuery(bookingQuery, arrayOf(roomId.toString(), checkIn, checkOut))
            bookingCursor.moveToFirst()
            val bookingCount = bookingCursor.getInt(0)
            bookingCursor.close()

            if (bookingCount == 0) {
                rooms.add(cursorToRoom(roomCursor))
            }
        }
        roomCursor.close()
        return rooms
    }

    fun isRoomAvailable(roomId: Int): Boolean {
        val db = readableDatabase
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        
        val query = """
            SELECT COUNT(*) FROM $TABLE_BOOKINGS 
            WHERE $COL_BOOKING_ROOM_ID = ? 
            AND $COL_BOOKING_CHECK_OUT > ?
        """.trimIndent()
        
        val cursor = db.rawQuery(query, arrayOf(roomId.toString(), today))
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        
        return count == 0
    }

    fun getRoomById(roomId: Int): Room? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_ROOMS,
            null,
            "$COL_ROOM_ID = ?",
            arrayOf(roomId.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val room = cursorToRoom(cursor)
            cursor.close()
            room
        } else {
            cursor.close()
            null
        }
    }

    private fun cursorToRoom(cursor: Cursor): Room {
        return Room(
            cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROOM_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COL_ROOM_NUMBER)),
            cursor.getString(cursor.getColumnIndexOrThrow(COL_ROOM_TYPE)),
            cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROOM_CAPACITY)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ROOM_PRICE)),
            cursor.getString(cursor.getColumnIndexOrThrow(COL_ROOM_DESCRIPTION)),
            cursor.getString(cursor.getColumnIndexOrThrow(COL_ROOM_IMAGE)),
            cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROOM_HAS_WIFI)) == 1,
            cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROOM_HAS_TV)) == 1,
            cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROOM_HAS_AC)) == 1,
            cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROOM_HAS_MINIBAR)) == 1,
            cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROOM_HAS_BALCONY)) == 1
        )
    }
}
