package com.example.lab4.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab4.data.entities.Term

@Dao
interface TermDao {
    @Query("SELECT * FROM terms")
    fun getAll(): LiveData<List<Term>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg terms: Term)
}