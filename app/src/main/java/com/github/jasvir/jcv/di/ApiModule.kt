package com.github.jasvir.jcv.di

import com.github.jasvir.jcv.data.api.ApiRepo
import com.github.jasvir.jcv.data.api.ApiRepository
import com.github.jasvir.jcv.data.api.ApiService
import com.github.jasvir.jcv.utils.isNetworkAvailable
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

/**
 * ApiModule
 *
 * Created by jasvir on 01-Feb-2020 at 11:34 AM.
 */

@ExperimentalTime
val apiModule = module {
    // Provide custom instance of [HttpLoggingInterceptor] to be attached to Retrofit for logging network calls.
    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        interceptor
    }

    // Provide [Interceptor] for adding headers for all requests.
    single(named("HeadersInterceptor")) {
        Interceptor { chain ->
            val request = chain.request().newBuilder()
            request.addHeader("Content-Type", "application/json")
            return@Interceptor chain.proceed(request.build())
        }
    }

    // Provide [Cache] to be used in caching network calls.
    single {
        val cacheDirectory = File(androidContext().cacheDir, "ImagePickerResponses")
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        Cache(cacheDirectory, cacheSize)
    }

    // Provide [Interceptor] for adding online and offline cache to network requests.
    single(named("NetworkInterceptor")) {
        Interceptor { chain ->
            val originalResponse = chain.proceed(chain.request())

            return@Interceptor originalResponse.newBuilder()
                .header(
                    "Cache-Control",
                    "public, max-age=${
                    if (isNetworkAvailable(androidContext()))
                        0
                    else
                        Duration.days(28).toDouble(DurationUnit.SECONDS).toLong()
                    }"
                )
                .build()
        }
    }

    // Provide custom instance of [OkHttpClient].
    single {
        val httpLoggingInterceptor: HttpLoggingInterceptor by inject()
        val headersInterceptor: Interceptor by inject(named("HeadersInterceptor"))
        val cache: Cache by inject()
        val networkInterceptor: Interceptor by inject(named("NetworkInterceptor"))

        val builder = OkHttpClient.Builder()
            .readTimeout(90, TimeUnit.SECONDS)
            .connectTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)

        builder.addInterceptor(headersInterceptor)
            .cache(cache)
            .addNetworkInterceptor(networkInterceptor)

        builder.build()
    }

    // Provide instance of [CoroutineCallAdapterFactory].
    single {
        CoroutineCallAdapterFactory()
    }

    // Provide instance of [MoshiConverterFactory].
    single {
        MoshiConverterFactory.create()
    }

    // Provide instance of [ApiService].
    single {
        val client: OkHttpClient by inject()
        val coroutineCallAdapterFactory: CoroutineCallAdapterFactory by inject()
        val moshiConverterFactory: MoshiConverterFactory by inject()

        Retrofit.Builder()
            .baseUrl("https://api.imagga.com/v2/")
            .client(client)
            .addCallAdapterFactory(coroutineCallAdapterFactory)
            .addConverterFactory(moshiConverterFactory)
            .build()
            .create(ApiService::class.java)
    }

    // Provide instance of [ApiRepository].
    single<ApiRepo> {
        ApiRepository(get())
    }

    // Provide instance of [Moshi] with date adapter.
    single {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
}