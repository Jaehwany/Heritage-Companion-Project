package com.ssafy.heritage.data.repository

import android.content.Context
import com.ssafy.heritage.data.dto.Heritage
import com.ssafy.heritage.data.dto.GroupAttribute
import com.ssafy.heritage.data.dto.HeritageScrap
import com.ssafy.heritage.data.dto.User
import com.ssafy.heritage.data.remote.api.RetrofitInstance.groupApi
import com.ssafy.heritage.data.remote.api.RetrofitInstance.heritageApi
import com.ssafy.heritage.data.remote.api.RetrofitInstance.userApi
import com.ssafy.heritage.data.remote.response.GroupListResponse
import com.ssafy.heritage.data.remote.response.HeritageReviewListResponse
import retrofit2.Response
import retrofit2.http.Body

class Repository constructor(context: Context) {

    // group
    suspend fun insertGroup(body: GroupListResponse): Response<GroupListResponse> = groupApi.insertGroup(body)
    suspend fun selectAllGroups(): Response<List<GroupListResponse>> = groupApi.selectAllGroups()
    suspend fun deleteGroup(groupSeq: Int): Response<Boolean> = groupApi.deleteGroup(groupSeq)
    suspend fun changeGroupActiveState(body: Int): Response<Boolean> = groupApi.changeGroupActiveState(body)
    suspend fun selectGroupMembers(groupSeq: Int): Response<List<User>> = groupApi.selectGroupMembers(groupSeq)
    suspend fun selectGroupDetail(groupSeq: Int): Response<GroupListResponse> = groupApi.selectGroupDetail(groupSeq)

    // user
    suspend fun checkEmail(userId: String): Response<String> = userApi.checkEmail(userId)
    suspend fun checkNickname(userNickname: String): Response<String> = userApi.checkNickname(userNickname)
    suspend fun signup(user: User): Response<String> = userApi.signup(user)
    suspend fun login(map: HashMap<String, String>): Response<String> = userApi.login(map)

    // heritage
    suspend fun selectAllHeritage(): Response<List<Heritage>> = heritageApi.selectAllHeritage()
    suspend fun insertHeritageReview(body: HeritageReviewListResponse): Response<HeritageReviewListResponse> = heritageApi.insertHeritageReview(body)
    suspend fun selectAllHeritageReviews(): Response<List<HeritageReviewListResponse>> = heritageApi.selectAllHeritageReviews()
    suspend fun insertHeritageScrap(scrap: HeritageScrap): Response<String> = heritageApi.insertHeritageScrap(scrap)
    suspend fun deleteHeritageScrap(userSeq: Int, heritageSeq: Int): Response<String> = heritageApi.deleteHeritageScrap(userSeq, heritageSeq)



    companion object {
        private var INSTANCE: Repository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Repository(context)
            }
        }

        fun get(): Repository {
            return INSTANCE ?: throw IllegalStateException("Repository must be initialized")
        }
    }
}