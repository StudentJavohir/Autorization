package com.example.authorization.Network

import com.example.authorization.utils.Utils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Network {

    lateinit var client: OkHttpClient


    // Объекта OkHttpClient, который предоставляет средства для работы с сетевыми запросами и ответами.
    private val Client = OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor(Utils.username, Utils.password))
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(Utils.clientTimeout, TimeUnit.SECONDS) // set the connection timeout to 30 seconds
        .readTimeout(Utils.clientTimeout, TimeUnit.SECONDS) // set the read timeout to 30 seconds
        .writeTimeout(Utils.clientTimeout, TimeUnit.SECONDS) // set the write timeout to 30 seconds
        .build()

    // Объекта Retrofit, который предоставляет средства для выполнения запросов к API.
    private val autoRetrofit = Retrofit.Builder()
        //Конвертер, который позволяет сериализовать и десериализовать JSON данные.
        .addConverterFactory(MoshiConverterFactory.create())
        .client(Client)
        .baseUrl(Utils.base_url + Utils.auth )
        .build()


    val Api: AutoApi = autoRetrofit.create(AutoApi::class.java)


    private val LoadingClient = OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor(Utils.username, Utils.password))
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(Utils.clientTimeout, TimeUnit.SECONDS) // set the connection timeout to 30 seconds
        .readTimeout(Utils.clientTimeout, TimeUnit.SECONDS) // set the read timeout to 30 seconds
        .writeTimeout(Utils.clientTimeout, TimeUnit.SECONDS) // set the write timeout to 30 seconds
        .build()

    // Объекта Retrofit, который предоставляет средства для выполнения запросов к API.
    private val loadingRetrofit = Retrofit.Builder()
        //Конвертер, который позволяет сериализовать и десериализовать JSON данные.
        .addConverterFactory(MoshiConverterFactory.create())
        .client(LoadingClient)
        .baseUrl(Utils.base_url + Utils.loading_auth)
        .build()

    // Объект CarApi, который будет использоваться для выполнения запросов к API. Оно будет иметь методы для выполнения запросов, определенные в интерфейсе CarApi.
    val loadingApi: LoadingApi = loadingRetrofit.create(LoadingApi::class.java)



    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .client(Client)
        .baseUrl(Utils.base_url + Utils.acceptance_auth)
        .build()

    val api: BaseApi = retrofit.create(BaseApi::class.java)



}