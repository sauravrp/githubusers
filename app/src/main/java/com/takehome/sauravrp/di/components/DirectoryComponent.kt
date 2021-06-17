package com.takehome.sauravrp.di.components

import com.takehome.sauravrp.di.components.modules.DirectoryViewModelFactoryModule
import com.takehome.sauravrp.di.scopes.DirectoryScope
import com.takehome.sauravrp.viewmodels.UserDirectoryViewModelFactory
import dagger.Component

@DirectoryScope
@Component(
    dependencies = [AppComponent::class],
    modules = [DirectoryViewModelFactoryModule::class]
)
interface DirectoryComponent {

    fun viewModelFactory(): UserDirectoryViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): DirectoryComponent
    }
}