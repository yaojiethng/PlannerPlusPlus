package com.orbital2019.plannerplusplus.model

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.orbital2019.plannerplusplus.model.entity.EventEntity
import com.orbital2019.plannerplusplus.model.entity.EventTaskRequirement
import com.orbital2019.plannerplusplus.model.entity.SubtaskEntity
import com.orbital2019.plannerplusplus.model.entity.TaskEntity


// todo:
//  1. properly save schema
//  2. Add database migration support
//  3. Add proper callback support and tests

@Database(
    entities = [EventEntity::class, TaskEntity::class, SubtaskEntity::class, EventTaskRequirement::class],
    version = 17,
    exportSchema = false
)

@TypeConverters(EventEntity.TiviTypeConverters::class)

// class is called AppDatabase as this app should only have one instance of database for both events and tasks.
abstract class AppDatabase : RoomDatabase() {

    // access database operation methods through automatically generated method
    abstract fun eventDao(): EventDao

    abstract fun taskDao(): TaskDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        // If you try running the above code with the created database above,
        // your app will crash as the operation performed is on the main thread.
        // By default, Room keeps a check of that and does not allow operations on the
        // main thread as it can makes your UI laggy.
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                // only one instance exists across all threads
                synchronized(AppDatabase::class) {
                    // databaseBuilder used for abstract class
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "app_database.db"
                    )
                        // when version number is incremented start with an empty database
                        .fallbackToDestructiveMigration()
                        // populates database with samples when it is first created
                        .addCallback(CALLBACK)
                        .build()
                }
            }
            return INSTANCE!!
        }

        // CALLBACK that populates Db when called with addCallBack
        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(INSTANCE!!)
                    .execute()
            }
        }

        // Populates Db with Sample Tasks
        private class PopulateDbAsyncTask constructor(private var database: AppDatabase) :
            AsyncTask<Void, Void, Void>() {

            val eventDao by lazy {
                database.eventDao()
            }

            val taskDao by lazy {
                database.taskDao()
            }

            override fun doInBackground(vararg params: Void?): Void? {
                taskDao.insert(TaskEntity(1, "DB_POP1", "", false, "", false))
                taskDao.insert(SubtaskEntity(null, "SUBTASK1", false, 1))
                taskDao.insert(SubtaskEntity(null, "SUBTASK2", true, 1))
                taskDao.insert(SubtaskEntity(null, "SUBTASK3", false, 1))

                eventDao.insert(EventEntity(id = 1, title = "TEST_EVENT"))
                eventDao.setEventRequirement(1, 1)

                return null
            }

        }
    }


    fun destroyInstance() {
        INSTANCE = null
    }
}