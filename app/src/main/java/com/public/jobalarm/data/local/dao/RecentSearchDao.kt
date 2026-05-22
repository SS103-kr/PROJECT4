package com.jobalarm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jobalarm.data.local.entity.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RecentSearchEntity): Long

    @Query("DELETE FROM recent_searches WHERE `query` = :q")
    suspend fun deleteByQuery(q: String)

    @Query("DELETE FROM recent_searches")
    suspend fun clear()

    @Query("SELECT * FROM recent_searches ORDER BY searchedAt DESC LIMIT 10")
    fun observeRecent(): Flow<List<RecentSearchEntity>>

    @Query("""
        DELETE FROM recent_searches WHERE id NOT IN
        (SELECT id FROM recent_searches ORDER BY searchedAt DESC LIMIT 10)
    """)
    suspend fun trim()
}
