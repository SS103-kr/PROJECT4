package com.jobalarm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jobalarm.data.local.entity.AlertOrgEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertOrgDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: AlertOrgEntity): Long

    @Query("DELETE FROM alert_orgs WHERE orgNm = :orgNm")
    suspend fun deleteByName(orgNm: String)

    @Query("SELECT * FROM alert_orgs ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<AlertOrgEntity>>

    @Query("SELECT * FROM alert_orgs")
    suspend fun getAll(): List<AlertOrgEntity>
}
