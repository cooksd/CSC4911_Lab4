package com.example.lab4.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "students")
data class Student(
    @PrimaryKey val studentId: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val firstTermId: String
)