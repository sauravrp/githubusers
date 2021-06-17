package com.takehome.sauravrp.di.components.modules

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.takehome.sauravrp.BuildConfig
import com.takehome.sauravrp.network.WebServicesAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * References:
 *  https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava3
 *  https://github.com/square/moshi
 *  https://github.com/square/retrofit/tree/master/retrofit-converters/moshi
 *  https://proandroiddev.com/moshi-with-retrofit-in-kotlin-%EF%B8%8F-a69c2621708b
 */
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun retrofit(): Retrofit {
        val okHttpBuilder = OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .apply {
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(StethoInterceptor())
            }
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpBuilder.build())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build()
    }

    @Singleton
    @Provides
    fun webservicesAPI(retrofit: Retrofit): WebServicesAPI {
        return retrofit.create(WebServicesAPI::class.java)
    }

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}