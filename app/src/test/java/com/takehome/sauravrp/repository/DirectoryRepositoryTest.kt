package com.takehome.sauravrp.repository

import com.google.common.truth.Truth.assertThat
import com.takehome.sauravrp.TestDataHelper
import com.takehome.sauravrp.db.AppDatabase
import com.takehome.sauravrp.db.UserDao
import com.takehome.sauravrp.network.WebServicesAPI
import com.takehome.sauravrp.viewmodels.User
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class DirectoryRepositoryTest {

    private lateinit var directoryRepository: DirectoryRepository

    @MockK
    lateinit var webServicesAPI: WebServicesAPI

    @MockK
    lateinit var appDatabase: AppDatabase

    @MockK
    lateinit var userDao : UserDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        directoryRepository = DirectoryRepository(webServicesAPI, appDatabase)
        every { appDatabase.userDao() } returns
                userDao
    }

    @Test
    fun `when services returns data, correct type is returned`() {
        val mockedObserver = TestObserver<List<User>>()

        val userDto = TestDataHelper.userDto()
        val usersDto = listOf(userDto)

        every { webServicesAPI.getUsers(1,1) } returns
                Single.just(usersDto)

        every { userDao.getAllUsers() } returns
                Single.just(listOf(userDto.toUser()))
        // when
        directoryRepository.getUserDirectory().subscribe(mockedObserver)

       // then
        assertThat(mockedObserver.values().first().first()).isInstanceOf(User::class.java)
    }

    @Test
    fun `when services returns error, error is returned`() {
        val mockedObserver = TestObserver<List<User>>()

        val throwable = Throwable("Some Error")

        every { webServicesAPI.getUsers(1,1) } returns
                Single.error(throwable)

        every { userDao.getAllUsers() } returns
                Single.error(throwable)
        // when
        directoryRepository.getUserDirectory().subscribe(mockedObserver)

        // then
        mockedObserver.assertError(throwable)
    }
}