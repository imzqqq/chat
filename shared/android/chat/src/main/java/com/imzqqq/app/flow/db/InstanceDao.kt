package com.imzqqq.app.flow.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single

@Dao
interface InstanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(instance: InstanceEntity)

    @Query("SELECT * FROM InstanceEntity WHERE instance = :instance LIMIT 1")
    fun loadMetadataForInstance(instance: String): Single<InstanceEntity>
}
