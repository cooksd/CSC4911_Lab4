package com.example.lab4.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab4.data.entities.CourseDetail

@Dao
interface CourseDetailDao {
    @Query("SELECT * FROM course_details")
    fun getAll(): LiveData<List<CourseDetail>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg courses: CourseDetail)
}