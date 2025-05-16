package com.example.lab4.ui.termdetail

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.example.lab4.data.AppDatabase
import com.example.lab4.data.entities.CourseDetail
import com.example.lab4.data.entities.Transcript
import com.example.lab4.repository.GpaRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData


class TermDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = GpaRepository(AppDatabase.getInstance(application))

    val courses: LiveData<List<CourseDetail>> = repo.courses

    fun transcriptsFor(studentId: String, termId: String): LiveData<List<Transcript>> =
        repo.transcriptsFor(studentId, termId)

    fun deleteTranscript(item: Transcript) = viewModelScope.launch {
        repo.deleteTranscript(item)
    }

    private val gradePoints = mapOf(
        "A"  to 4.0,
        "AB" to 3.5,
        "B"  to 3.0,
        "BC" to 2.5,
        "C"  to 2.0,
        "CD" to 1.5,
        "D"  to 1.0,
        "F"  to 0.0
    )

    private fun computeGpa(transcripts: List<Transcript>, courses: List<CourseDetail>): Double {
        val totalPoints = transcripts.mapNotNull { tr ->
            val course = courses.find { it.courseDetailId == tr.courseDetailId }
            val gp     = gradePoints[tr.grade]
            if (course != null && gp != null) course.credits * gp else null
        }.sum()
        val totalCredits = transcripts.mapNotNull { tr ->
            courses.find { it.courseDetailId == tr.courseDetailId }?.credits
        }.sum()
        return if (totalCredits > 0) totalPoints / totalCredits else 0.0
    }


    fun buildSummary(
        studentName: String,
        termName: String,
        transcripts: List<Transcript>,
        courses: List<CourseDetail>
    ): String {
        val classCount  = transcripts.size
        val creditSum   = transcripts.mapNotNull { tr ->
            courses.find { it.courseDetailId == tr.courseDetailId }?.credits
        }.sum()
        val gpa         = computeGpa(transcripts, courses)
        return "$studentName – $termName\n" +
                "Classes: $classCount  Credits: $creditSum  GPA: ${"%.2f".format(gpa)}"
    }
}