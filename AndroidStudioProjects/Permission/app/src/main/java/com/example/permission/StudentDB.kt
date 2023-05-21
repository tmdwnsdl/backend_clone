package com.example.permission

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[Student::class], version=1)
abstract class StudentDB : RoomDatabase() {
    abstract fun studentDAO(): StudentDAO
    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: StudentDB? = null
        fun getInstance(context: Context): StudentDB {
// if the INSTANCE is not null, then return it,
// if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentDB::class.java,
                    "student_database"
                ).allowMainThreadQueries()
                .build()
                INSTANCE = instance
// return instance
                instance
            }
        }
    }
}
