package com.nazmar.dicegainz.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nazmar.dicegainz.PREF_KEY_NUM_ROLL_CARDS
import com.nazmar.dicegainz.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Repository {

    private lateinit var db: LiftDatabase
    private lateinit var liftDao: LiftDao
    private lateinit var tagDao: TagDao
    private lateinit var prefs: SharedPreferences

    lateinit var allTagsList: LiveData<List<String>>

    fun setDataSource(dataBase: LiftDatabase) {
        db = dataBase
        liftDao = db.liftDao
        tagDao = db.tagDao
        allTagsList = getAllTags()
    }

    // ------------------------------Card number preferences----------------------------------

    private var _numCards = MutableLiveData(3)

    val numCards: LiveData<Int>
        get() = _numCards

    fun setPreferences(preferences: SharedPreferences) {
        prefs = preferences
        _numCards.value = prefs.getInt(PREF_KEY_NUM_ROLL_CARDS, 3)
    }

    fun addCard() {
        _numCards.value = _numCards.value!! + 1
        prefs.edit {
            putInt(PREF_KEY_NUM_ROLL_CARDS, numCards.value!!)
        }
    }

    fun removeCard() {
        _numCards.value = _numCards.value!! - 1
        prefs.edit {
            putInt(PREF_KEY_NUM_ROLL_CARDS, numCards.value!!)
        }
    }

    // ------------------------------Queries----------------------------------

    suspend fun getLift(liftId: Long): Lift? = liftDao.getLift(liftId)

    fun getAllLifts() = liftDao.getAllLifts()

    suspend fun getAllLiftsOneShot() = liftDao.getAllLiftsOneShot()

    suspend fun getLiftsForTagOneShot(tag: String) = liftDao.getLiftsForTagOneShot(tag)

    fun getLiftsForTag(tag: String) = liftDao.getLiftsForTag(tag)

    private fun getAllTags() = tagDao.getAllTagNames()

    suspend fun getTagNamesForLift(id: Long) = tagDao.getTagNamesForLift(id)

    // ------------------------------Editing----------------------------------

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