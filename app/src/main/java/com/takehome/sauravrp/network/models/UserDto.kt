package com.takehome.sauravrp.network.models

import com.squareup.moshi.Json

data class UserDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "login") val userName: String,
    @field:Json(name = "avatar_url") val photoUrl: String,
    @field:Json(name = "type") val type: String,
)