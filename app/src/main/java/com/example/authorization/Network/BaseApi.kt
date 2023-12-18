package com.example.authorization.Network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

//Интерфейс определяет, какие методы и параметры необходимы для взаимодействия клиента и сервера, но не реализует их.
interface BaseApi {

    @GET("AUTO")
    fun auth(): Call<ResponseBody>


    @GET("Priemki")
    fun acceptanceList(): Call<ResponseBody>

    @GET("TipiUpakovki")
    fun packageList(): Call<ResponseBody>

    @GET("VidiTovarov")
    fun productTypeList(): Call<ResponseBody>


}


interface LoadingApi{
    // Метод HTTP GET для получения списка машин из сервера. Метод вернет объект Call с телом ответа ResponseBody
    @GET("autorization")
    fun loadAuth(): Call<ResponseBody>

    @GET("cars")
    fun carsList(): Call<ResponseBody>



    @GET("Sklads")
    fun warehouseList(): Call<ResponseBody>
}


interface AutoApi{
    @GET("AUTO")
    fun auto(): Call<ResponseBody>
}