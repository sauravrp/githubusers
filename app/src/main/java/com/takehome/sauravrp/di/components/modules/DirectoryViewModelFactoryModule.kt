package com.takehome.sauravrp.di.components.modules

import com.takehome.sauravrp.di.scopes.DirectoryScope
import com.takehome.sauravrp.repository.DirectoryRepository
import com.takehome.sauravrp.viewmodels.UserDirectoryViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class DirectoryViewModelFactoryModule  {

    @Provides
    @DirectoryScope
    fun providesViewModelFactory(directoryRepository: DirectoryRepository) : UserDirectoryViewModelFactory {
        return UserDirectoryViewModelFactory(directoryRepository)
    }
}