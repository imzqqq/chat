package com.imzqqq.app.flow.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imzqqq.app.flow.components.conversation.ConversationEntity

@Dao
interface ConversationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversations: List<ConversationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversation: ConversationEntity): Long

    @Delete
    suspend fun delete(conversation: ConversationEntity): Int

    @Query("SELECT * FROM ConversationEntity WHERE accountId = :accountId ORDER BY s_createdAt DESC")
    fun conversationsForAccount(accountId: Long): PagingSource<Int, ConversationEntity>

    @Query("DELETE FROM ConversationEntity WHERE accountId = :accountId")
    fun deleteForAccount(accountId: Long)
}
