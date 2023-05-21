package com.example.permission

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id:Int,
    val firstName: String,
    val lastName: String,
    val score: Int,
    val grade: String
)