package com.nazmar.dicegainz

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nazmar.dicegainz.database.*
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

    private lateinit var tagDao: TagDao
    private lateinit var liftDao: LiftDao
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
        liftDao = db.liftDao
        tagDao = db.tagDao
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
        val lift = Lift("Front Squat", 1, 1L)
        runBlocking {
            liftDao.insert(lift)
            assertEquals(lift, liftDao.getLift(lift.id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateLift() {
        val lift = Lift("Front Squat", 1, 1L)
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
        val lift = Lift("Front Squat", 1, 1L)
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
                Lift(i.toString(), i % 2, i + 1L).run {
                    lifts.add(this)
                    liftDao.insert(this)
                }
            }
            lifts.sortBy { it.name }
            assertEquals(lifts, liftDao.getAllLiftsTest())
        }
    }

    /*
    * Tag tests
    */
    @Test
    @Throws(Exception::class)
    fun insertTag() {
        val lift = Lift("Front Squat", 1, 1L)
        val tag = Tag("Push", lift.id)
        runBlocking {
            liftDao.insert(lift)
            tagDao.insert(tag)
            assertEquals(tag, tagDao.getTag(tag.name, tag.liftId))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteTag() {
        val lift = Lift("Front Squat", 1, 1L)
        val tag = Tag("Push", lift.id)
        runBlocking {
            liftDao.insert(lift)
            tagDao.run {
                insert(tag)
                delete(tag)
            }
            assertEquals(null, tagDao.getTag(tag.name, tag.liftId))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() {
        val tags = mutableListOf<Tag>()
        runBlocking {
            for (name in 'z' downTo 'a') {
                val id = liftDao.insert(Lift(name.toString(), 1))
                tags.add(Tag(name.toString(), id))
            }
            tagDao.insertAll(tags)
            tagDao.deleteAll(tags)
            assertEquals(emptyList<String>(), tagDao.getAllTagNamesTest())
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertAll() {
        val tags = mutableListOf<Tag>()
        runBlocking {
            for (name in 'z' downTo 'a') {
                val id = liftDao.insert(Lift(name.toString(), 1))
                tags.add(Tag(name.toString(), id))
            }
            tagDao.insertAll(tags)
            assertEquals(tags.map { it.name }.asReversed(), tagDao.getAllTagNamesTest())
        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllTags() {
        val tags = mutableListOf<String>()
        runBlocking {
            for (name in 'z' downTo 'a') {
                var id = liftDao.insert(Lift(name.toString(), 1))
                tags.add(name.toString())
                tagDao.insert(Tag(name.toString(), id))
                id = liftDao.insert(Lift(name.toString(), 2))
                tagDao.insert(Tag(name.toString(), id))
            }
            assertEquals(tags.asReversed(), tagDao.getAllTagNamesTest())
        }
    }

    /*
    * Cascade tests
    */
    @Test
    @Throws(Exception::class)
    fun cascadeDelete() {
        val lift = Lift("Front Squat", 1, 1L)
        val tag = Tag("Push", lift.id)
        runBlocking {
            liftDao.insert(lift)
            tagDao.insert(tag)
            liftDao.delete(lift)
            assertEquals(null, tagDao.getTag(tag.name, tag.liftId))
        }
    }

    @Test
    @Throws(Exception::class)
    fun cascadeDeleteMultipleTags() {
        val lift = Lift("Front Squat", 1, 1L)
        val tag1 = Tag("Push", lift.id)
        val tag2 = Tag("Pull", lift.id)
        runBlocking {
            liftDao.insert(lift)
            tagDao.run {
                insert(tag1)
                insert(tag2)
                delete(tag1)
            }
            assertEquals(null, tagDao.getTag(tag1.name, tag1.liftId))
            assertEquals(tag2, tagDao.getTag(tag2.name, tag2.liftId))
        }
    }

    /*
     * Complex tests
     */
    @Test
    @Throws(Exception::class)
    fun getTagNamesForLift() {
        val lift = Lift("Front Squat", 1, 1L)
        val names = mutableListOf<String>()
        val tags = mutableListOf<Tag>()
        runBlocking {
            liftDao.insert(lift)
            for (name in 'z' downTo 'a') {
                names.add(name.toString())
                Tag(name.toString(), lift.id).run {
                    tags.add(this)
                    tagDao.insert(this)
                }
            }
            assertEquals(names.sorted(), tagDao.getTagNamesForLiftTest(lift.id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun getLiftsForTag() {
        val lift1 = Lift("Lift 1", 0, 1)
        val lift2 = Lift("Lift 2", 1, 2)
        val lift3 = Lift("Lift 3", 2, 3)
        val lifts = listOf(lift1, lift2)
        val names = listOf("Push", "Pull", "Legs")
        val tags = mutableListOf<Tag>()
        runBlocking {
            liftDao.insertAll(listOf(lift1, lift2, lift3))
            tagDao.insertAll(
                names.map { name ->
                    Tag(name, lift1.id).also { tags.add(it) }
                }
            )
            tagDao.insert(Tag("Push", 2))
            assertEquals(lifts, liftDao.getLiftsForTagTest("Push"))
        }
    }
}
