package com.example.lab4.repository

import androidx.lifecycle.LiveData
import com.example.lab4.data.AppDatabase
import com.example.lab4.data.entities.Student
import com.example.lab4.data.entities.Term
import com.example.lab4.data.entities.CourseDetail
import com.example.lab4.data.entities.Transcript

class GpaRepository(private val db: AppDatabase) {
    val students: LiveData<List<Student>>      = db.studentDao.getAll()
    val terms:    LiveData<List<Term>>         = db.termDao.getAll()
    val courses:  LiveData<List<CourseDetail>> = db.courseDetailDao.getAll()

    fun transcriptsForStudent(studentId: String): LiveData<List<Transcript>> =
        db.transcriptDao.forStudent(studentId)

    fun transcriptsFor(studentId: String, termId: String): LiveData<List<Transcript>> =
        db.transcriptDao.forStudentTerm(studentId, termId)

    suspend fun addOrUpdateTranscript(t: Transcript) =
        db.transcriptDao.upsert(t)

    suspend fun deleteTranscript(t: Transcript) =
        db.transcriptDao.delete(t)
}