package com.jobalarm.data.repository

import com.jobalarm.data.local.dao.BookmarkDao
import com.jobalarm.data.local.entity.BookmarkEntity
import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkRepositoryImpl @Inject constructor(
    private val dao: BookmarkDao
) : BookmarkRepository {

    override fun observeAll(): Flow<List<BookmarkEntity>> = dao.observeAll()

    override fun observeIsBookmarked(sn: String): Flow<Boolean> = dao.observeExists(sn)

    override suspend fun toggle(posting: JobPosting) {
        if (dao.exists(posting.recrutPbancSn)) {
            dao.deleteBySn(posting.recrutPbancSn)
        } else {
            dao.insert(
                BookmarkEntity(
                    recrutPbancSn = posting.recrutPbancSn,
                    instNm = posting.instNm,
                    recrutPbancTtl = posting.recrutPbancTtl,
                    pbancEndYmd = posting.pbancEndYmd,
                    savedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
