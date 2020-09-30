package com.ramzan.dicegainz

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.database.LiftDatabaseDao
import com.ramzan.dicegainz.database.Tag
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

    /*
     * Lift tests
     */
    @Test
    @Throws(Exception::class)
    fun insertLift() {
        val lift = Lift(1L, "Front Squat", 1)
        runBlocking {
            liftDao.insert(lift)
            assertEquals(lift, liftDao.getLift(lift.id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateLift() {
        val lift = Lift(1L, "Front Squat", 1)
        runBlocking {
            liftDao.insert(lift)
            lift.name = "Bench Press"
            liftDao.update(lift)
            assertEquals(lift, liftDao.getLift(lift.id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteLift() {
        val lift = Lift(1L, "Front Squat", 1)
        runBlocking {
            liftDao.insert(lift)
            liftDao.delete(lift)
            assertEquals(null, liftDao.getLift(lift.id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllLifts() {
        val lifts = mutableListOf<Lift>()
        runBlocking {
            for (i in 0..20) {
                val lift = Lift(i + 1L, i.toString(), i % 2)
                lifts.add(lift)
                liftDao.insert(lift)
            }
            assertEquals(lifts, liftDao.getAllLiftsTest())
        }
    }

    /*
    * Tag tests
    */
    @Test
    @Throws(Exception::class)
    fun insertTag() {
        val lift = Lift(1L, "Front Squat", 1)
        val tag = Tag("Push", lift.id)
        runBlocking {
            liftDao.insert(lift)
            liftDao.insert(tag)
            assertEquals(tag, liftDao.getTag(tag.name, tag.liftId))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteTag() {
        val lift = Lift(1L, "Front Squat", 1)
        val tag = Tag("Push", lift.id)
        runBlocking {
            liftDao.insert(lift)
            liftDao.insert(tag)
            liftDao.delete(tag)
            assertEquals(null, liftDao.getTag(tag.name, tag.liftId))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() {
        val tags = mutableListOf<Tag>()
        runBlocking {
            for (name in 'z' downTo 'a') {
                val id = liftDao.insert(Lift(name.toString(), 1))
                val tag = Tag(name.toString(), id)
                tags.add(tag)
            }
            liftDao.insertAll(tags)
            liftDao.deleteAll(tags)
            assertEquals(emptyList<String>(), liftDao.getAllTagNamesTest())
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertAll() {
        val tags = mutableListOf<Tag>()
        runBlocking {
            for (name in 'z' downTo 'a') {
                val id = liftDao.insert(Lift(name.toString(), 1))
                val tag = Tag(name.toString(), id)
                tags.add(tag)
            }
            liftDao.insertAll(tags)
            assertEquals(tags.map {it.name}.asReversed(), liftDao.getAllTagNamesTest())
        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllTags() {
        val tags = mutableListOf<String>()
        runBlocking {
            for (name in 'z' downTo 'a') {
                var id = liftDao.insert(Lift(name.toString(), 1))
                var tag = Tag(name.toString(), id)
                tags.add(name.toString())
                liftDao.insert((tag))
                id = liftDao.insert(Lift(name.toString(), 2))
                tag = Tag(name.toString(), id)
                liftDao.insert((tag))
            }
            assertEquals(tags.asReversed(), liftDao.getAllTagNamesTest())
        }
    }

    /*
    * Cascade tests
    */
    @Test
    @Throws(Exception::class)
    fun cascadeDelete() {
        val lift = Lift(1L, "Front Squat", 1)
        val tag = Tag("Push", lift.id)
        runBlocking {
            liftDao.insert(lift)
            liftDao.insert(tag)
            liftDao.delete(lift)
            assertEquals(null, liftDao.getTag(tag.name, tag.liftId))
        }
    }

    @Test
    @Throws(Exception::class)
    fun cascadeDeleteMultipleTags() {
        val lift = Lift(1L, "Front Squat", 1)
        val tag1 = Tag("Push", lift.id)
        val tag2 = Tag("Pull", lift.id)
        runBlocking {
            liftDao.insert(lift)
            liftDao.insert(tag1)
            liftDao.insert(tag2)
            liftDao.delete(tag1)
            assertEquals(null, liftDao.getTag(tag1.name, tag1.liftId))
            assertEquals(tag2, liftDao.getTag(tag2.name, tag2.liftId))
        }
    }

    /*
     * Complex tests
     */
    @Test
    @Throws(Exception::class)
    fun getTagNamesForLift() {
        val lift = Lift(1L, "Front Squat", 1)
        val names = mutableListOf<String>()
        val tags = mutableListOf<Tag>()
        runBlocking {
            liftDao.insert(lift)
            for (name in 'a'..'z') {
                val tag = Tag(name.toString(), lift.id)
                tags.add(tag)
                names.add(name.toString())
                liftDao.insert((tag))
            }
            assertEquals(names, liftDao.getTagNamesForLiftTest(lift.id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun getLiftsForTag() {
        val lift1 = Lift(1, "Lift 1", 0)
        val lift2 = Lift(2, "Lift 2", 1)
        val lift3 = Lift(3, "Lift 3", 2)
        val lifts = listOf(lift1, lift2)
        val names = listOf("Push", "Pull", "Legs")
        val tags = mutableListOf<Tag>()
        runBlocking {
            liftDao.insert(lift1)
            liftDao.insert(lift2)
            liftDao.insert(lift3)
            for (name in names) {
                val tag = Tag(name, lift1.id)
                tags.add(tag)
                liftDao.insert((tag))
            }
            liftDao.insert(Tag("Push", 2))
            assertEquals(lifts, liftDao.getLiftsForTagTest("Push"))
        }
    }
}
