package com.example.lab4.ui.addclass

import android.app.Application
import androidx.lifecycle.*
import com.example.lab4.data.AppDatabase
import com.example.lab4.data.entities.Student
import com.example.lab4.data.entities.Term
import com.example.lab4.data.entities.Transcript
import com.example.lab4.repository.GpaRepository
import kotlinx.coroutines.launch

class AddClassViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = GpaRepository(AppDatabase.getInstance(application))

    val courses = repo.courses
    val students: LiveData<List<Student>> = repo.students
    val terms:    LiveData<List<Term>>    = repo.terms

    fun studentName(fullList: List<com.example.lab4.data.entities.Student>, id: String): String =
        fullList.find { it.studentId == id }
            ?.let { "${it.firstName} ${it.lastName}" }
            .orEmpty()

    fun termName(fullList: List<com.example.lab4.data.entities.Term>, id: String): String =
        fullList.find { it.termId == id }?.name.orEmpty()

    fun addOrUpdateTranscript(
        studentId: String,
        termId: String,
        courseDetailId: String,
        grade: String
    ) = viewModelScope.launch {
        val row = Transcript(
            studentId       = studentId,
            termId          = termId,
            courseDetailId = courseDetailId,
            grade           = grade
        )
        repo.addOrUpdateTranscript(row)
    }
}