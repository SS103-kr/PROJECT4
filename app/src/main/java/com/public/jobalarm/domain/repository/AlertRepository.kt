package com.jobalarm.domain.repository

import com.jobalarm.domain.model.AlertOrg
import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    fun observeAll(): Flow<List<AlertOrg>>
    suspend fun getAll(): List<AlertOrg>
    suspend fun add(orgNm: String, orgClsfNm: String)
    suspend fun remove(orgNm: String)
    suspend fun hasNotified(sn: String): Boolean
    suspend fun existingNotified(sns: List<String>): List<String>
    suspend fun markNotified(sn: String)
}
