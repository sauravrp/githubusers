package com.takehome.sauravrp

import android.app.Application
import com.takehome.sauravrp.di.components.AppComponent
import com.takehome.sauravrp.di.components.DirectoryComponent

class TestApplication :
    Application(),
    AppComponentProvider,
    DirectoryComponentProvider {

    lateinit var mockApplicationComponent: AppComponent

    lateinit var mockDirectoryComponent: DirectoryComponent

    override fun appComponent(): AppComponent = mockApplicationComponent
    override fun directoryComponent(): DirectoryComponent = mockDirectoryComponent
}
