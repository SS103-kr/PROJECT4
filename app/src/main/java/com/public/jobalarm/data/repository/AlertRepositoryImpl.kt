package com.jobalarm.data.repository

import com.jobalarm.data.local.dao.AlertOrgDao
import com.jobalarm.data.local.dao.NotifiedPostDao
import com.jobalarm.data.local.entity.AlertOrgEntity
import com.jobalarm.data.local.entity.NotifiedPostEntity
import com.jobalarm.domain.model.AlertOrg
import com.jobalarm.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertRepositoryImpl @Inject constructor(
    private val alertDao: AlertOrgDao,
    private val notifiedDao: NotifiedPostDao
) : AlertRepository {

    override fun observeAll(): Flow<List<AlertOrg>> =
        alertDao.observeAll().map { list ->
            list.map { AlertOrg(it.orgNm, it.orgClsfNm, it.addedAt) }
        }

    override suspend fun getAll(): List<AlertOrg> =
        alertDao.getAll().map { AlertOrg(it.orgNm, it.orgClsfNm, it.addedAt) }

    override suspend fun add(orgNm: String, orgClsfNm: String) {
        alertDao.insert(
            AlertOrgEntity(orgNm = orgNm, orgClsfNm = orgClsfNm, addedAt = System.currentTimeMillis())
        )
    }

    override suspend fun remove(orgNm: String) = alertDao.deleteByName(orgNm)

    override suspend fun hasNotified(sn: String): Boolean =
        notifiedDao.existing(listOf(sn)).isNotEmpty()

    override suspend fun existingNotified(sns: List<String>): List<String> =
        if (sns.isEmpty()) emptyList() else notifiedDao.existing(sns)

    override suspend fun markNotified(sn: String) {
        notifiedDao.insert(NotifiedPostEntity(recrutPbancSn = sn, notifiedAt = System.currentTimeMillis()))
    }
}
