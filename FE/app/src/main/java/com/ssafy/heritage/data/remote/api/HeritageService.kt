package com.ssafy.heritage.data.remote.api

import com.ssafy.heritage.data.dto.Heritage
import com.ssafy.heritage.data.dto.HeritageScrap
import com.ssafy.heritage.data.remote.response.HeritageReviewListResponse
import retrofit2.Response
import retrofit2.http.*

interface HeritageService {

    // 전체 문화유산 목록 가져온다
    @GET("/api/heritage/list")
    suspend fun selectAllHeritage(): Response<List<Heritage>>

    // 문화유산 스크랩을 추가한다
    @POST("/api/heritage/scrap")
    suspend fun insertHeritageScrap(@Body scrap: HeritageScrap ): Response<String>

    // 문화유산 스크랩을 삭제한다
    @DELETE("/api/mypage/scrap/{userSeq}/{heritageSeq}")
    suspend fun deleteHeritageScrap(@Path("userSeq") userSeq: Int, @Path("heritageSeq") heritageSeq: Int): Response<String>

    // 해당 문화유산의 전체 리뷰를 가져온다
    @GET("/api/heritage/reviews")
    suspend fun selectAllHeritageReviews(): Response<List<HeritageReviewListResponse>>

    // 리뷰를 작성한다
    @POST("/api/heritage/review")
    suspend fun insertHeritageReview(@Body body: HeritageReviewListResponse): Response<HeritageReviewListResponse>

    // 스터디에 공유한다다

    // 문화유산 정렬해서 져오기??
    // 문화유산 상세정보 가져오기??
}