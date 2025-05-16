package com.example.lab4.ui.overview

import android.app.Application
import androidx.lifecycle.*
import com.example.lab4.data.AppDatabase
import com.example.lab4.data.entities.CourseDetail
import com.example.lab4.data.entities.Student
import com.example.lab4.data.entities.Term
import com.example.lab4.data.entities.Transcript
import com.example.lab4.repository.GpaRepository

class StudentOverviewViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = GpaRepository(AppDatabase.getInstance(application))

    val students: LiveData<List<Student>> = repo.students

    val courses: LiveData<List<CourseDetail>> = repo.courses

    private val _selectedStudentId = MutableLiveData<String>()

    fun selectStudent(studentId: String) {
        _selectedStudentId.value = studentId
    }

    val termsForSelected: LiveData<List<Term>> = _selectedStudentId.switchMap { sid ->
        MediatorLiveData<List<Term>>().apply {
            val termsSource = repo.terms
            val transSource = repo.transcriptsForStudent(sid)

            fun update() {
                val allTerms = termsSource.value.orEmpty()
                val allTrans = transSource.value.orEmpty()
                val attended = allTrans.map { it.termId }.distinct()
                value = allTerms.filter { it.termId in attended }
            }

            addSource(termsSource) { update() }
            addSource(transSource) { update() }
        }
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
            val gp = gradePoints[tr.grade]
            if (course != null && gp != null) course.credits * gp else null
        }.sum()

        val totalCredits = transcripts.mapNotNull { tr ->
            courses.find { it.courseDetailId == tr.courseDetailId }?.credits
        }.sum()

        return if (totalCredits > 0) totalPoints / totalCredits else 0.0
    }

    val overallGpa: LiveData<Double> = _selectedStudentId.switchMap { sid ->
        MediatorLiveData<Double>().apply {
            val transSource = repo.transcriptsForStudent(sid)
            val coursesSrc  = repo.courses

            fun update() {
                val trans = transSource.value.orEmpty()
                val crs   = coursesSrc.value.orEmpty()
                value = computeGpa(trans, crs)
            }

            addSource(transSource) { update() }
            addSource(coursesSrc)  { update() }
        }
    }
}