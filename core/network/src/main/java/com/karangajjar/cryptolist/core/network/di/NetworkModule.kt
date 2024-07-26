package com.karangajjar.cryptolist.core.network.di

import android.os.Build
import com.karangajjar.cryptolist.core.network.NetworkDataSource
import com.karangajjar.cryptolist.core.network.NetworkDataSourceImpl
import com.karangajjar.cryptolist.core.network.retrofit.RetrofitApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import java.time.Duration

const val BASE_URL = "https://api.coincap.io/v2/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): RetrofitApi = retrofit.create(RetrofitApi::class.java)


    @Provides
    @Singleton
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OkHttpClient.Builder().addNetworkInterceptor(loggingInterceptor)
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(30))
                .build()
        } else {
            OkHttpClient.Builder().addNetworkInterceptor(loggingInterceptor)
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideNetworkDataSource(api: RetrofitApi): NetworkDataSource {
        return NetworkDataSourceImpl(api)
    }
}