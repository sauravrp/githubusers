package com.takehome.sauravrp

import com.takehome.sauravrp.network.models.UserDto

object TestDataHelper {
    fun userDto() = UserDto(id = "1",
        userName = "John",
        photoUrl = "smallUrl",
        type = "User")
}