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
    fun insertAndGetLift() {
        val name = "Front Squat"
        val lift = Lift(name = name)
        runBlocking {
            liftDao.insert(lift)
            val addedLift = liftDao.get(name)
            assertEquals(name, addedLift?.name)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTiers() {
        val name = "Front Squat"
        val tier = 0
        val lift = Lift(name = name, tier = tier)
        runBlocking {
            liftDao.insert(lift)
            val addedLift = liftDao.get(name)
            assertEquals(tier, addedLift?.tier)
        }
    }
}
