package com.ugrcaan.qreader.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.StateFlow

@Dao
interface LinkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLink(link: Link)

    @Query("SELECT * FROM links")
    fun readAllLink(): LiveData<List<Link>>

    @Update
    suspend fun updateLink(link: Link)

    @Delete
    suspend fun deleteLink(link: Link)

    @Query("DELETE FROM links")
    suspend fun deleteAllLinks()
}