package com.takehome.sauravrp.components.modules

import androidx.lifecycle.ViewModel
import com.takehome.sauravrp.components.scopes.TestScope
import com.takehome.sauravrp.repository.DirectoryRepository
import com.takehome.sauravrp.viewmodels.UserDirectoryViewModel
import com.takehome.sauravrp.viewmodels.UserDirectoryViewModelFactory
import dagger.Module
import dagger.Provides
import io.mockk.mockk

@Module
class MockDirectoryViewModelFactoryModule(private val userDirectoryViewModel: UserDirectoryViewModel) {

    @Provides
    @TestScope
    fun directoryRepository(): DirectoryRepository = mockk()

    @Provides
    @TestScope
    fun providesViewModelFactory(directoryRepository: DirectoryRepository): UserDirectoryViewModelFactory {
        return object : UserDirectoryViewModelFactory(directoryRepository) {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return userDirectoryViewModel as T
            }
        }
    }
}