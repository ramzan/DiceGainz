package com.nazmar.dicegainz.repository

import androidx.lifecycle.LiveData
import com.nazmar.dicegainz.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Repository {

    private lateinit var db: LiftDatabase
    private lateinit var liftDao: LiftDao
    private lateinit var tagDao: TagDao
    lateinit var allTagsList: LiveData<List<String>>

    fun setDataSource(dataBase: LiftDatabase) {
        db = dataBase
        liftDao = db.liftDao
        tagDao = db.tagDao
        allTagsList = getAllTags()
    }

    suspend fun getLift(liftId: Long): Lift? = liftDao.getLift(liftId)

    fun getAllLifts() = liftDao.getAllLifts()

    suspend fun getAllLiftsOneShot() = liftDao.getAllLiftsOneShot()

    fun getLiftsForTag(tag: String) = liftDao.getLiftsForTag(tag)

    suspend fun getLiftsForTagOneShot(tag: String) = liftDao.getLiftsForTagOneShot(tag)


    private fun getAllTags() = tagDao.getAllTagNames()

    suspend fun getTagNamesForLift(id: Long) = tagDao.getTagNamesForLift(id)

    fun addLift(lift: Lift, tags: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.runInTransaction {
                CoroutineScope(Dispatchers.IO).launch {
                    val id = liftDao.insert(lift)
                    tagDao.insertAll(tags.map { Tag(it, id) })
                }
            }
        }
    }

    fun updateLift(lift: Lift, newTags: List<Tag>, deletedTags: List<Tag>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.runInTransaction {
                CoroutineScope(Dispatchers.IO).launch {
                    liftDao.update(lift)
                    tagDao.insertAll(newTags)
                    tagDao.deleteAll(deletedTags)
                }
            }
        }
    }

    fun deleteLift(lift: Lift) {
        CoroutineScope(Dispatchers.IO).launch {
            liftDao.delete(lift)
        }
    }
}