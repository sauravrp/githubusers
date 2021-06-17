package com.takehome.sauravrp.components.modules

import com.takehome.sauravrp.components.scopes.TestScope
import com.takehome.sauravrp.db.AppDatabase
import dagger.Module
import dagger.Provides
import io.mockk.mockk

@Module
class MockDbModule {

    @Provides
    @TestScope
    fun appDb() : AppDatabase = mockk()
}