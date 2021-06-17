package com.takehome.sauravrp.repository

import com.google.common.truth.Truth.assertThat
import com.takehome.sauravrp.TestDataHelper
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TestNetworkDtoTransformation {

    @Test
    fun testUserDtoTransformation() {
        val userDto = TestDataHelper.userDto()

        val user = userDto.toUser()

        assertThat(user.id).isEqualTo(userDto.id)
        assertThat(user.userName).isEqualTo(userDto.userName)
        assertThat(user.photoUrl).isEqualTo(userDto.photoUrl)
        assertThat(user.type).isEqualTo(userDto.type)
    }

}