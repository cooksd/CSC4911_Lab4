package com.example.lab4.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "course_details")
data class CourseDetail(
    @PrimaryKey val courseDetailId: String = UUID.randomUUID().toString(),
    val catalogId: String,
    val description: String,
    val credits: Int
)