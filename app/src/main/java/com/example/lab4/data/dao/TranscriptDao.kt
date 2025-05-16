package com.example.lab4.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab4.data.entities.Transcript

@Dao
interface TranscriptDao {
    @Query("SELECT * FROM transcripts WHERE studentId = :studentId")
    fun forStudent(studentId: String): LiveData<List<Transcript>>

    @Query("SELECT * FROM transcripts WHERE studentId = :studentId AND termId = :termId")
    fun forStudentTerm(studentId: String, termId: String): LiveData<List<Transcript>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(transcript: Transcript)

    @Delete
    suspend fun delete(transcript: Transcript)
}