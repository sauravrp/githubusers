package com.takehome.sauravrp.repository

import com.takehome.sauravrp.db.AppDatabase
import com.takehome.sauravrp.di.scopes.DirectoryScope
import com.takehome.sauravrp.network.WebServicesAPI
import com.takehome.sauravrp.network.models.UserDto
import com.takehome.sauravrp.viewmodels.User
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@DirectoryScope
class DirectoryRepository @Inject constructor(
        private val webServicesAPI: WebServicesAPI,
        private val database: AppDatabase) {

    companion object {
        const val COUNT = 20
        const val PAGE = 1
    }

    fun getUserDirectory(): Single<List<User>> {
        return database.userDao().getAllUsers().flatMap { cachedData ->
            if (cachedData.isEmpty()) {
                webServicesAPI
                        .getUsers(COUNT, PAGE)
                        .map { userList ->
                            userList.map { it.toUser() }
                        }
                        .flatMap { networkData ->
                            database
                                    .userDao()
                                    .insertAllUsers(*networkData.toTypedArray())
                                    .andThen(Single.just(networkData))
                        }
            } else {
                Single.just(cachedData)
            }
        }
    }
}

// Network dto converted to models consumed by views
fun UserDto.toUser(): User {
    return User(
            id = id,
            userName = userName,
            photoUrl = photoUrl.orEmpty(),
            type = type
    )
}