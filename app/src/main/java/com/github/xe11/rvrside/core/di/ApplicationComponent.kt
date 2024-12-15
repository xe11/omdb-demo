package com.github.xe11.rvrside.core.di

import android.content.Context
import com.github.xe11.rvrside.core.data.MovieRepositoryImpl
import com.github.xe11.rvrside.core.data.MoviesApi
import com.github.xe11.rvrside.core.data.SearchRepositoryImpl
import com.github.xe11.rvrside.core.domain.MovieRepository
import com.github.xe11.rvrside.core.domain.SearchRepository
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Logger.Companion.DEFAULT
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Component(
    modules = [
        SearchModule::class,
        MoviesModule::class,
        NetworkModule::class,
    ],
)
@Singleton
internal interface ApplicationComponent {

    fun movieRepository(): MovieRepository

    fun searchRepository(): SearchRepository

    companion object {
        fun formContext(context: Context): ApplicationComponent {
            return context.applicationContext
                .getSystemService(ApplicationComponent::class.java.name) as ApplicationComponent
        }
    }
}

@Module
internal interface SearchModule {

    @Binds
    @Singleton
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}

@Module
internal interface MoviesModule {

    @Binds
    fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository

    companion object {

        @Provides
        fun provideMoviesApi(retrofit: Retrofit): MoviesApi =
            retrofit.create()
    }
}

@Module
internal interface NetworkModule {

    companion object {

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor(DEFAULT).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            return OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(
            json: Json,
            okHttpClient: OkHttpClient,
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com") // TODO [Alexei Laban]: extract to config
                .client(okHttpClient)
                .addConverterFactory(
                    json.asConverterFactory("application/json; charset=UTF8".toMediaType())
                )
                .build()
        }

        @Provides
        @Singleton
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
            }
        }
    }
}
