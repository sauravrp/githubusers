package com.takehome.sauravrp

import android.app.Application
import com.facebook.stetho.Stetho
import com.squareup.picasso.Picasso
import com.takehome.sauravrp.di.components.AppComponent
import com.takehome.sauravrp.di.components.DaggerAppComponent
import com.takehome.sauravrp.di.components.DaggerDirectoryComponent
import com.takehome.sauravrp.di.components.DirectoryComponent
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import timber.log.Timber

class DirectoryApplication : Application(), AppComponentProvider, DirectoryComponentProvider {

    private lateinit var applicationComponent: AppComponent
    private lateinit var directoryComponentVar: DirectoryComponent

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
            Picasso.get().apply {
                setIndicatorsEnabled(true)
                isLoggingEnabled = true
            }
        }

        RxJavaPlugins.setErrorHandler(Timber::e)
    }

    override fun appComponent(): AppComponent {
       if(!::applicationComponent.isInitialized) {
           applicationComponent = DaggerAppComponent.builder().bindAppContext(applicationContext).build()
       }
        return  applicationComponent
    }

    override fun directoryComponent(): DirectoryComponent {
        if(!::directoryComponentVar.isInitialized) {
            directoryComponentVar =  DaggerDirectoryComponent
                .factory()
                .create(appComponent())
        }
        return  directoryComponentVar
    }
}

interface AppComponentProvider {
    fun appComponent(): AppComponent
}

interface DirectoryComponentProvider {
    fun directoryComponent(): DirectoryComponent
}
