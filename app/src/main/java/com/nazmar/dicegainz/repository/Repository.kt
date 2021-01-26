package com.nazmar.dicegainz.repository

import androidx.lifecycle.LiveData
import com.nazmar.dicegainz.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Repository {

    private lateinit var liftDao: LiftDao
    private lateinit var tagDao: TagDao
    lateinit var allTagsList: LiveData<List<String>>

    fun setDataSource(db: LiftDatabase) {
        liftDao = db.liftDao
        tagDao = db.tagDao
        allTagsList = getAllTags()
    }

    fun getAllLifts(): LiveData<List<Lift>> {
        return liftDao.getAllLifts()
    }

    fun getLiftsForTag(tag: String): LiveData<List<Lift>> {
        return liftDao.getLiftsForTag(tag)
    }

    private fun getAllTags(): LiveData<List<String>> {
        return tagDao.getAllTagNames()
    }

    fun getTagNamesForLift(id: Long): LiveData<List<String>> {
        return tagDao.getTagNamesForLift(id)
    }

    fun addLift(lift: Lift, tags: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val id = liftDao.insert(lift)
            tagDao.insertAll(tags.map { Tag(it, id) })
        }
    }


    fun updateLift(lift: Lift, newTags: List<Tag>, deletedTags: List<Tag>) {
        CoroutineScope(Dispatchers.IO).launch {
            liftDao.update(lift)
            tagDao.insertAll(newTags)
            tagDao.deleteAll(deletedTags)
        }
    }

    fun deleteLift(lift: Lift) {
        CoroutineScope(Dispatchers.IO).launch {
            liftDao.delete(lift)
        }
    }
}