package com.example.lab4.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab4.data.entities.Student

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAll(): LiveData<List<Student>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg students: Student)
}