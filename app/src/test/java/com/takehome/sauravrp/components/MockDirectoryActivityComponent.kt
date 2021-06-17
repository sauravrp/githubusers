package com.takehome.sauravrp.components

import com.takehome.sauravrp.components.modules.MockDbModule
import com.takehome.sauravrp.components.modules.MockDirectoryViewModelFactoryModule
import com.takehome.sauravrp.components.modules.MockNetworkModule
import com.takehome.sauravrp.components.scopes.TestScope
import com.takehome.sauravrp.di.components.AppComponent
import com.takehome.sauravrp.di.components.DirectoryComponent
import dagger.Component

@TestScope
@Component(
    modules = [MockDirectoryViewModelFactoryModule::class,
        MockNetworkModule::class,
        MockDbModule::class]
)
interface MockDirectoryActivityComponent : DirectoryComponent, AppComponent