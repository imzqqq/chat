package com.imzqqq.app.flow.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DraftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(draft: DraftEntity)

    @Query("SELECT * FROM DraftEntity WHERE accountId = :accountId ORDER BY id ASC")
    fun draftsPagingSource(accountId: Long): PagingSource<Int, DraftEntity>

    @Query("SELECT * FROM DraftEntity WHERE accountId = :accountId")
    suspend fun loadDrafts(accountId: Long): List<DraftEntity>

    @Query("DELETE FROM DraftEntity WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM DraftEntity WHERE id = :id")
    suspend fun find(id: Int): DraftEntity?
}
