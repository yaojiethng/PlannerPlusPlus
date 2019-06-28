package com.orbital2019.plannerplusplus.model

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.threeten.bp.LocalDateTime

@Database(
    entities = [PlannerEvent::class],
    version = 1,
    exportSchema = false // todo: properly save schema
)

// class is called AppDatabase as this app should only have one instance of database for both events and tasks.
abstract class AppDatabase : RoomDatabase() {

    // access database operation methods through automatically generated method
    abstract fun eventDao(): EventDao
    // abstract fun taskDao() : TaskDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        // If you try running the above code with the created database above,
        // your app will crash as the operation performed is on the main thread.
        // By default, Room keeps a check of that and doesn’t allow operations on the
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

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(INSTANCE!!)
                    .execute()
                //db.execSQL("CREATE TRIGGER ...")
            }
        }

        private class PopulateDbAsyncTask constructor(private var database: AppDatabase) :
            AsyncTask<Void, Void, Void>() {

            val eventDao by lazy {
                database.eventDao()
            }

            override fun doInBackground(vararg params: Void?): Void? {
                eventDao.insert(
                    PlannerEvent(
                        "Test", LocalDateTime.now(), null, true,
                        followUp = true,
                        tags = mutableListOf("Hi", "Test")
                    )
                )
                eventDao.insert(
                    PlannerEvent(
                        "Test2", LocalDateTime.now(), null, true,
                        followUp = false,
                        tags = mutableListOf("Hi")
                    )
                )
                eventDao.insert(
                    PlannerEvent(
                        "Test3", LocalDateTime.now(), null, false,
                        followUp = true,
                        tags = mutableListOf("Test")
                    )
                )
                return null
            }
        }
    }


    fun destroyInstance() {
        INSTANCE = null
    }
}