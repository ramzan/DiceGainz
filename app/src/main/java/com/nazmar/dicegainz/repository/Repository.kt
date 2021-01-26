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

    fun getAllLifts(): LiveData<List<Lift>> {
        return liftDao.getAllLifts()
    }

    fun getLiftsForTag(tag: String): LiveData<List<Lift>> {
        return liftDao.getLiftsForTag(tag)
    }

    private fun getAllTags(): LiveData<List<String>> {
        return tagDao.getAllTagNames()
    }

    suspend fun getTagNamesForLift(id: Long): List<String> {
        return tagDao.getTagNamesForLift(id)
    }

    fun addLift(lift: Lift, tags: List<String>) {
        db.runInTransaction {
            CoroutineScope(Dispatchers.IO).launch {
                val id = liftDao.insert(lift)
                tagDao.insertAll(tags.map { Tag(it, id) })
            }
        }
    }

    fun updateLift(lift: Lift, newTags: List<Tag>, deletedTags: List<Tag>) {
        db.runInTransaction {
            CoroutineScope(Dispatchers.IO).launch {
                liftDao.update(lift)
                tagDao.insertAll(newTags)
                tagDao.deleteAll(deletedTags)
            }
        }
    }

    fun deleteLift(lift: Lift) {
        CoroutineScope(Dispatchers.IO).launch {
            liftDao.delete(lift)
        }
    }
}