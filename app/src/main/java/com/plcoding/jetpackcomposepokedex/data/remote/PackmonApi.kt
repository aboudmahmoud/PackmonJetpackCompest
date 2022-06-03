package com.plcoding.jetpackcomposepokedex.data.remote

import com.plcoding.jetpackcomposepokedex.data.remote.response.Packmon
import com.plcoding.jetpackcomposepokedex.data.remote.response.PackmonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PackmonApi {

    @GET("pokemon")
    suspend fun getPackmonList(
        @Query("limit") limit: Int,
        @Query("offest") offset: Int
    ): PackmonList

    @GET("pokemon/{name}")
    suspend fun getPokmenInfo(
        @Path("name") name: String
    ): Packmon

}