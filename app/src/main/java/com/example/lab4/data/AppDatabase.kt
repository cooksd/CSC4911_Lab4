package com.example.lab4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lab4.data.dao.CourseDetailDao
import com.example.lab4.data.dao.StudentDao
import com.example.lab4.data.dao.TermDao
import com.example.lab4.data.dao.TranscriptDao
import com.example.lab4.data.entities.CourseDetail
import com.example.lab4.data.entities.Student
import com.example.lab4.data.entities.Term
import com.example.lab4.data.entities.Transcript
import com.example.lab4.utils.XmlDataParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Student::class, Term::class, CourseDetail::class, Transcript::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val studentDao: StudentDao
    abstract val termDao: TermDao
    abstract val courseDetailDao: CourseDetailDao
    abstract val transcriptDao: TranscriptDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gpa.db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    XmlDataParser(context).parseAndPopulate(database)
                                }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
    }
}