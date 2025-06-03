package com.example.kubstu_app.data.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kubstu_app.data.local.dao.AppDao
import com.example.kubstu_app.data.local.entities.*

@Database(
    entities = [
        DiscEntity::class,
        ExamScheduleEntity::class,
        FormaObEntity::class,
        GradesEntity::class,
        GroupEntity::class,
        InstituteEntity::class,
        KindOfNagrEntity::class,
        LessonEntity::class,
        SpecsEntity::class,
        StudentEntity::class,
        StudentNoteEntity::class,
        WorkEntity::class,
    ],

    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kubstu_app_database"
                )
                    .fallbackToDestructiveMigration(true) // при изменении схемы БД удаляет старую БД
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}