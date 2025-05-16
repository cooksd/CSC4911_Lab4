package com.example.lab4.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "transcripts",
    foreignKeys = [
        ForeignKey(entity = Student::class, parentColumns = ["studentId"], childColumns = ["studentId"], onDelete = CASCADE),
        ForeignKey(entity = Term::class, parentColumns = ["termId"],   childColumns = ["termId"],    onDelete = CASCADE),
        ForeignKey(entity = CourseDetail::class, parentColumns = ["courseDetailId"], childColumns = ["courseDetailId"], onDelete = CASCADE)
    ],
    indices = [Index("studentId"), Index("termId"), Index("courseDetailId")]
)
data class Transcript(
    @PrimaryKey val transcriptDetailId: String = UUID.randomUUID().toString(),
    val studentId: String,
    val termId: String,
    val courseDetailId: String,
    val grade: String
)