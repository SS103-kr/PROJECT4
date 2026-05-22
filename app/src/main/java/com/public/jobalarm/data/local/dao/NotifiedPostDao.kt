package com.jobalarm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jobalarm.data.local.entity.NotifiedPostEntity

@Dao
interface NotifiedPostDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: NotifiedPostEntity): Long

    @Query("SELECT recrutPbancSn FROM notified_posts WHERE recrutPbancSn IN (:sns)")
    suspend fun existing(sns: List<String>): List<String>

    @Query("DELETE FROM notified_posts WHERE notifiedAt < :before")
    suspend fun purgeOlderThan(before: Long)
}
