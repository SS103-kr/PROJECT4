package com.jobalarm.data.remote.api

import com.jobalarm.data.remote.dto.AlioRecruitResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AlioRecruitApi {

    @GET("recruitment/list")
    suspend fun getJobs(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 100,
        @Query("instType") instType: String? = null,
        @Query("ongoingYn") ongoingYn: String? = null,
        @Query("recrutPbancTtl") recrutPbancTtl: String? = null,
        @Query("pblntInstCd") pblntInstCd: String? = null,
        @Query("type") type: String = "json"
    ): AlioRecruitResponse
}
