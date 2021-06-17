package com.takehome.sauravrp.db

import androidx.room.*
import com.takehome.sauravrp.viewmodels.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAllUsers(): Single<List<User>>

    @Insert
    fun insertAllUsers(vararg users: User): Completable
}