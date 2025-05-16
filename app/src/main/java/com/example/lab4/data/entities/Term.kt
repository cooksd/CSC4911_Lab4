package com.example.lab4.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "terms")
data class Term(
    @PrimaryKey val termId: String = UUID.randomUUID().toString(),
    val name: String,
    val startDate: Long
)