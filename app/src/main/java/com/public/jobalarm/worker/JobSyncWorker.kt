package com.jobalarm.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jobalarm.domain.repository.AlertRepository
import com.jobalarm.domain.repository.JobRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class JobSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val jobRepository: JobRepository,
    private val alertRepository: AlertRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = try {
        jobRepository.refresh(pageNo = 1).getOrNull()
        val orgs = alertRepository.getAll()
        orgs.forEach { org ->
            val jobs = jobRepository.fetchByOrg(org.orgNm).getOrNull().orEmpty()
            val sns = jobs.map { it.recrutPbancSn }.filter { it.isNotBlank() }
            val already = alertRepository.existingNotified(sns).toSet()
            jobs.forEach { job ->
                if (job.recrutPbancSn.isNotBlank() && job.recrutPbancSn !in already) {
                    notificationHelper.show(
                        context = applicationContext,
                        orgName = job.instNm.ifBlank { org.orgNm },
                        title = job.recrutPbancTtl,
                        recrutPbancSn = job.recrutPbancSn
                    )
                    alertRepository.markNotified(job.recrutPbancSn)
                }
            }
        }
        Result.success()
    } catch (_: Exception) {
        Result.retry()
    }
}
