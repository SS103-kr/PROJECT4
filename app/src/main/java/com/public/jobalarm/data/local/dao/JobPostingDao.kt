package com.jobalarm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jobalarm.data.local.entity.JobPostingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobPostingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<JobPostingEntity>)

    @Query("SELECT * FROM job_postings ORDER BY pbancBgngYmd DESC")
    fun observeAllLatest(): Flow<List<JobPostingEntity>>

    @Query("SELECT * FROM job_postings ORDER BY pbancEndYmd ASC")
    fun observeAllByDeadline(): Flow<List<JobPostingEntity>>

    @Query("SELECT * FROM job_postings ORDER BY instNm ASC")
    fun observeAllByOrgName(): Flow<List<JobPostingEntity>>

    @Query("SELECT * FROM job_postings WHERE instClsfCd = :code ORDER BY pbancBgngYmd DESC")
    fun observeByCategory(code: String): Flow<List<JobPostingEntity>>

    @Query("SELECT * FROM job_postings WHERE recrutPbancSn = :sn LIMIT 1")
    suspend fun getBySn(sn: String): JobPostingEntity?

    @Query("SELECT * FROM job_postings WHERE recrutPbancSn = :sn LIMIT 1")
    fun observeBySn(sn: String): Flow<JobPostingEntity?>

    @Query("""
        SELECT * FROM job_postings
        WHERE recrutPbancTtl LIKE '%' || :q || '%' OR instNm LIKE '%' || :q || '%'
        ORDER BY pbancBgngYmd DESC
    """)
    fun search(q: String): Flow<List<JobPostingEntity>>

    @Query("SELECT COUNT(*) FROM job_postings WHERE instClsfCd = :code")
    fun countByCategory(code: String): Flow<Int>

    @Query("SELECT * FROM job_postings WHERE instNm LIKE '%' || :instNm || '%' ORDER BY pbancBgngYmd DESC")
    suspend fun getByInstNm(instNm: String): List<JobPostingEntity>

    @Query("DELETE FROM job_postings WHERE savedAt < :before")
    suspend fun purgeOlderThan(before: Long)
}
