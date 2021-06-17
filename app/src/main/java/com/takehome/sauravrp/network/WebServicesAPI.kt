package com.takehome.sauravrp.network

import com.takehome.sauravrp.network.models.UserDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServicesAPI {

    @GET("users")
    fun getUsers(
        @Query("per_page") count: Int,
        @Query("page") page: Int
    ): Single<List<UserDto>>

}