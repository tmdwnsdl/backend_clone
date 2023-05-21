package com.example.permission

import androidx.room.*

@Dao
interface StudentDAO {
    @Delete
    fun delete(student: Student)

    @Query("SELECT * FROM students")
    fun getAll(): List<Student>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(student: Student)

    @Insert
    fun insertTwoStudents(student1: Student, student2: Student)

    @Insert
    fun insertStudents(studentList:List<Student>)

    @Update
    fun updateStudent(student: Student)

    @Query("SELECT firstName from students where score > :minScore")
    fun searchStudent(minScore: Int): List<String>
}
