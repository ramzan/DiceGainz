package com.nazmar.dicegainz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [Lift::class, Tag::class], version = 1, exportSchema = false)
abstract class LiftDatabase : RoomDatabase() {

    abstract val liftDao: LiftDao
    abstract val tagDao: TagDao


    companion object {
        @Volatile
        private var INSTANCE: LiftDatabase? = null

        fun getInstance(context: Context): LiftDatabase {
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LiftDatabase::class.java,
                        "lift_database"
                    )
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                GlobalScope.launch(Dispatchers.IO) {
                                    withContext(Dispatchers.IO) {
                                        getInstance(context).apply {
                                            liftDao.insertAll(PREPOPULATE_LIFTS)
                                            tagDao.insertAll(PREPOPULATE_TAGS)
                                        }
                                    }
                                }
                            }
                        })
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }

        val PREPOPULATE_LIFTS = listOf(
            Lift("Squat", BOTH),
            Lift("Bench Press", BOTH),
            Lift("Deadlift", BOTH),
            Lift("Overhead Press", BOTH),
            Lift("Front Squat", BOTH),
            Lift("Barbell Row", BOTH),
            Lift("Incline Press", BOTH),
            Lift("Pull Up", BOTH),
            Lift("Chin Up", BOTH),
            Lift("Dips", BOTH)
        )
        private const val PULL = "Pull"
        private const val PUSH = "Push"
        private const val LEGS = "Legs"
        private const val UPPER = "Upper"
        private const val LOWER = "Lower"
        val PREPOPULATE_TAGS = listOf(
            Tag(LEGS, 1),
            Tag(LOWER, 1),
            Tag(UPPER, 2),
            Tag(PUSH, 2),
            Tag(LEGS, 3),
            Tag(LOWER, 3),
            Tag(UPPER, 4),
            Tag(PUSH, 4),
            Tag(LEGS, 5),
            Tag(LOWER, 5),
            Tag(UPPER, 6),
            Tag(PULL, 6),
            Tag(UPPER, 7),
            Tag(PUSH, 7),
            Tag(UPPER, 8),
            Tag(PULL, 8),
            Tag(UPPER, 9),
            Tag(PULL, 9),
            Tag(UPPER, 10),
            Tag(PUSH, 10)
        )
    }
}