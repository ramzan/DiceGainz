package com.ramzan.dicegainz

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.database.LiftDatabaseDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class LiftDatabaseTest {

    private lateinit var liftDao: LiftDatabaseDao
    private lateinit var db: LiftDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, LiftDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        liftDao = db.liftDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insert() {
        val name = "Front Squat"
        val tier = 1
        val lift = Lift(name, tier)
        runBlocking {
            liftDao.insert(lift)
            val addedLift = liftDao.get(lift.id)
            assertEquals(lift, addedLift)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllLifts() {
        runBlocking {

            val arr: Array<Lift?> = arrayOfNulls(21)
            for (i in 0..20) {
                arr[i] = Lift(i.toString(), i % 2)
                arr[i]?.let { liftDao.insert(it) }
            }
            val lifts = listOf(arr)
            val allLifts = liftDao.getAllLifts()
            assertEquals(lifts, allLifts.value)
        }
    }
}
