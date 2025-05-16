package com.example.lab4.utils

import android.content.Context
import com.example.lab4.R
import com.example.lab4.data.AppDatabase
import com.example.lab4.data.entities.Student
import com.example.lab4.data.entities.Term
import com.example.lab4.data.entities.CourseDetail
import com.example.lab4.data.entities.Transcript

class XmlDataParser(private val ctx: Context) {

    suspend fun parseAndPopulate(db: AppDatabase) {
        val studentsRaw = readRaw(R.raw.students)
        val students = studentsRaw
            .split("-")
            .mapNotNull { parseStudent(it) }
        db.studentDao.insertAll(*students.toTypedArray())

        val termsRaw = readRaw(R.raw.terms)
        val terms = termsRaw
            .split("-")
            .mapNotNull { parseTerm(it) }
        db.termDao.insertAll(*terms.toTypedArray())

        val coursesRaw = readRaw(R.raw.coursedetails)
        val courses = coursesRaw
            .split("-")
            .mapNotNull { parseCourseDetail(it) }
        db.courseDetailDao.insertAll(*courses.toTypedArray())

        val transcriptsRaw = readRaw(R.raw.transcripts)
        val transcripts = transcriptsRaw
            .split("-")
            .mapNotNull { parseTranscript(it) }
        transcripts.forEach { db.transcriptDao.upsert(it) }
    }

    private fun readRaw(resId: Int): String =
        ctx.resources.openRawResource(resId)
            .bufferedReader()
            .use { it.readText() }

    private fun extractTag(xml: String, tag: String): String? {
        val regex = "<$tag>(.*?)</$tag>".toRegex(RegexOption.DOT_MATCHES_ALL)
        return regex.find(xml)?.groups?.get(1)?.value?.trim()
    }

    private fun parseStudent(xml: String): Student? {
        val id    = extractTag(xml, "studentId") ?: return null
        val first = extractTag(xml, "firstName") ?: return null
        val last  = extractTag(xml, "lastName") ?: return null
        val term  = extractTag(xml, "firstTermId") ?: return null
        return Student(studentId = id, firstName = first, lastName = last, firstTermId = term)
    }

    private fun parseTerm(xml: String): Term? {
        val id    = extractTag(xml, "termId") ?: return null
        val name  = extractTag(xml, "name") ?: return null
        val date  = extractTag(xml, "startDate")?.toLongOrNull() ?: return null
        return Term(termId = id, name = name, startDate = date)
    }

    private fun parseCourseDetail(xml: String): CourseDetail? {
        val id    = extractTag(xml, "courseDetailId") ?: return null
        val cat   = extractTag(xml, "catalogId") ?: return null
        val desc  = extractTag(xml, "description") ?: return null
        val creds = extractTag(xml, "credits")?.toIntOrNull() ?: return null
        return CourseDetail(
            courseDetailId = id,
            catalogId      = cat,
            description    = desc,
            credits        = creds
        )
    }

    private fun parseTranscript(xml: String): Transcript? {
        val id    = extractTag(xml, "transcriptDetailId") ?: return null
        val sid   = extractTag(xml, "studentId") ?: return null
        val tid   = extractTag(xml, "termId") ?: return null
        val cdid  = extractTag(xml, "courseDetailId") ?: return null
        val grade = extractTag(xml, "grade") ?: return null
        return Transcript(
            transcriptDetailId = id,
            studentId          = sid,
            termId             = tid,
            courseDetailId     = cdid,
            grade              = grade
        )
    }
}