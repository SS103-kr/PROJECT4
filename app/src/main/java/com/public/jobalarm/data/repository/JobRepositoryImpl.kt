package com.jobalarm.data.repository

import com.jobalarm.data.local.dao.JobPostingDao
import com.jobalarm.data.mapper.toDomain
import com.jobalarm.data.mapper.toEntity
import com.jobalarm.data.remote.api.AlioRecruitApi
import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.model.JobSort
import com.jobalarm.domain.repository.JobRepository
import com.jobalarm.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobRepositoryImpl @Inject constructor(
    private val api: AlioRecruitApi,
    private val dao: JobPostingDao
) : JobRepository {

    // instType → (instClsfCd, instClsfNm)
    private val categoryGroups = listOf(
        Triple("A2001", "A", "공기업"),
        Triple("A2002", "A", "공기업"),
        Triple("A2003", "B", "준정부기관"),
        Triple("A2004", "B", "준정부기관"),
        Triple("A2005", "C", "기타공공기관")
    )

    override fun observeAll(sort: JobSort): Flow<List<JobPosting>> {
        val source = when (sort) {
            JobSort.LATEST -> dao.observeAllLatest()
            JobSort.DEADLINE -> dao.observeAllByDeadline()
            JobSort.ORG_NAME -> dao.observeAllByOrgName()
        }
        return source.map { list -> list.map { it.toDomain() } }
    }

    override fun observeByCategory(code: String): Flow<List<JobPosting>> =
        dao.observeByCategory(code).map { list -> list.map { it.toDomain() } }

    override fun observeBySn(sn: String): Flow<JobPosting?> =
        dao.observeBySn(sn).map { it?.toDomain() }

    override fun categoryCount(code: String): Flow<Int> = dao.countByCategory(code)

    override suspend fun refresh(pageNo: Int, instNm: String?): Result<Int> = runCatching {
        var totalCount = 0
        for ((instType, catCd, catNm) in categoryGroups) {
            val resp = api.getJobs(
                serviceKey = BuildConfig.PUBLIC_DATA_API_KEY,
                pageNo = pageNo,
                numOfRows = 100,
                instType = instType,
                ongoingYn = "Y"
            )
            if (resp.resultCode != 200) {
                error("API 오류 [${resp.resultCode}]: ${resp.resultMsg}")
            }
            val entities = resp.result.mapNotNull { it.toEntity(instClsfCd = catCd, instClsfNm = catNm) }
            if (entities.isNotEmpty()) dao.upsertAll(entities)
            totalCount += resp.totalCount
        }
        totalCount
    }

    override suspend fun fetchByOrg(instNm: String): Result<List<JobPosting>> = runCatching {
        dao.getByInstNm(instNm).map { it.toDomain() }
    }
}
