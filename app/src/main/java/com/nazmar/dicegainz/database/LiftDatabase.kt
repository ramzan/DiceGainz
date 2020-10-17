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

/**
 * A database that stores Lift information.
 * And a global method to get access to the database.
 */
@Database(entities = [Lift::class, Tag::class], version = 1, exportSchema = false)
abstract class LiftDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val liftDatabaseDao: LiftDatabaseDao

    /**
     * Define a companion object, this allows us to add functions on the LiftDatabase class.
     *
     * For example, clients can call `LiftDatabase.getInstance(context)` to instantiate
     * a new LiftDatabase.
     */
    companion object {
        /**
         * INSTANCE will keep a reference to any database returned via getInstance.
         *
         * This will help us avoid repeatedly initializing the database, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: LiftDatabase? = null

        /**
         * Helper function to get the database.
         *
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         *
         * This function is threadsafe, and callers should cache the result for multiple database
         * calls to avoid overhead.
         *
         * This is an example of a simple Singleton pattern that takes another Singleton as an
         * argument in Kotlin.
         *
         * To learn more about Singleton read the wikipedia article:
         * https://en.wikipedia.org/wiki/Singleton_pattern
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context): LiftDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {

                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE

                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LiftDatabase::class.java,
                        "lift_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this lesson. You can learn more about
                        // migration with Room in this blog post:
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        // prepopulate the database after onCreate was called
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                // moving to a new thread
                                GlobalScope.launch(Dispatchers.IO) {
                                    withContext(Dispatchers.IO) {
                                        getInstance(context).liftDatabaseDao.apply {
                                            insertLifts(PREPOPULATE_LIFTS)
                                            insertTags(PREPOPULATE_TAGS)
                                        }
                                    }
                                }
                            }
                        })
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }

        val PREPOPULATE_LIFTS = listOf(
            Lift(1, "Squat", BOTH),
            Lift(2, "Bench Press", BOTH),
            Lift(3, "Deadlift", BOTH),
            Lift(4, "Overhead Press", BOTH),
            Lift(5, "Front Squat", BOTH),
            Lift(6, "Barbell Row", BOTH),
            Lift(7, "Incline Press", BOTH),
            Lift(8, "Pull Up", BOTH),
            Lift(9, "Chin Up", BOTH),
            Lift(10, "Dips", BOTH)
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