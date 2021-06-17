package com.takehome.sauravrp.components.modules

import com.takehome.sauravrp.components.scopes.TestScope
import com.takehome.sauravrp.network.WebServicesAPI
import dagger.Module
import dagger.Provides
import io.mockk.mockk

@Module
class MockNetworkModule {

    @Provides
    @TestScope
    fun webservicesAPI() : WebServicesAPI = mockk()
}