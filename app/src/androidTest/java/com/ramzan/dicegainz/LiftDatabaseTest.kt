package com.ramzan.dicegainz

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ramzan.dicegainz.database.*
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
        val id = 1
        val lift = Lift(id, "Front Squat", 1)
        runBlocking {
            liftDao.insert(lift)
            assertEquals(lift, liftDao.getLift(id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateLift() {
        val id = 1
        val lift = Lift(id, "Front Squat", 1)
        runBlocking {
            liftDao.insert(lift)
            lift.name = "Bench Press"
            liftDao.update(lift)
            assertEquals(lift, liftDao.getLift(id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteLift() {
        val id = 1
        val lift = Lift(id, "Front Squat", 1)
        runBlocking {
            liftDao.insert(lift)
            liftDao.delete(lift)
            assertEquals(null, liftDao.getLift(id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllLifts() {
        val lifts = mutableListOf<Lift>()
        runBlocking {
            for (i in 0..20) {
                val lift = Lift(i + 1, i.toString(), i % 2)
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
        val name = "Push"
        val tag = Tag(name)
        runBlocking {
            liftDao.insert(tag)
            assertEquals(tag, liftDao.getTag(name))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteTag() {
        val name = "Push"
        val tag = Tag(name)
        runBlocking {
            liftDao.insert(tag)
            liftDao.delete(tag)
            assertEquals(null, liftDao.getTag(name))
        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllTags() {
        val tags = mutableListOf<Tag>()
        runBlocking {
            for (i in 'a'..'z') {
                val tag = Tag(i.toString())
                tags.add(tag)
                liftDao.insert(tag)
            }
            assertEquals(tags, liftDao.getAllTagsTest())
        }
    }

    /*
 * Tag tests
 */
    @Test
    @Throws(Exception::class)
    fun insertTagLift() {
        val liftId = 1
        val lift = Lift(liftId, "Front Squat", 1)
        val name = "Push"
        val tag = Tag(name)
        val tagLift = TagLift(tag.name, lift.id)
        runBlocking {
            liftDao.insert(lift)
            liftDao.insert(tag)
            liftDao.insert(tagLift)
            assertEquals(tagLift, liftDao.getTagLift(name, liftId))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteTagLift() {
        val liftId = 1
        val lift = Lift(liftId, "Front Squat", 1)
        val name = "Push"
        val tag = Tag(name)
        val tagLift = TagLift(tag.name, lift.id)
        runBlocking {
            liftDao.insert(lift)
            liftDao.insert(tag)
            liftDao.insert(tagLift)
            liftDao.delete(tagLift)
            assertEquals(null, liftDao.getTagLift(name, liftId))
        }
    }

    @Test
    @Throws(Exception::class)
    fun cascadeDelete() {
        val liftId = 1
        val lift = Lift(liftId, "Front Squat", 1)
        val name = "Push"
        val tag = Tag(name)
        val tagLift = TagLift(tag.name, lift.id)
        runBlocking {
            liftDao.insert(lift)
            liftDao.insert(tag)
            liftDao.insert(tagLift)
            liftDao.delete(lift)
            assertEquals(null, liftDao.getTagLift(name, liftId))
        }
    }

    @Test
    @Throws(Exception::class)
    fun cascadeDeleteMultipleTags() {
        val liftId = 1
        val lift = Lift(liftId, "Front Squat", 1)
        val name1 = "Push"
        val name2 = "Pull"
        val tag1 = Tag(name1)
        val tag2 = Tag(name2)
        val tagLift1 = TagLift(tag1.name, lift.id)
        val tagLift2 = TagLift(tag2.name, lift.id)
        runBlocking {
            liftDao.insert(lift)
            liftDao.insert(tag1)
            liftDao.insert(tag2)
            liftDao.insert(tagLift1)
            liftDao.insert(tagLift2)
            liftDao.delete(tag1)
            assertEquals(null, liftDao.getTagLift(name1, liftId))
            assertEquals(tagLift2, liftDao.getTagLift(name2, liftId))
        }
    }

    /*
     * Complex tests
     */
    @Test
    @Throws(Exception::class)
    fun getTagNamesForLift() {
        val liftId = 1
        val lift = Lift(liftId, "Front Squat", 1)
        val names = listOf("Push", "Pull", "Legs")
        val tags = mutableListOf<Tag>()
        runBlocking {
            liftDao.insert(lift)
            for (name in names) {
                val tag = Tag(name)
                tags.add(tag)
                liftDao.insert((tag))
                liftDao.insert(TagLift(name, liftId))
            }
            assertEquals(names, liftDao.getTagNamesForLiftTest(liftId))
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
                val tag = Tag(name)
                tags.add(tag)
                liftDao.insert((tag))
                liftDao.insert(TagLift(name, 1))
            }
            liftDao.insert(TagLift("Push", 2))
            assertEquals(lifts, liftDao.getLiftsForTagTest("Push"))
        }
    }
}
