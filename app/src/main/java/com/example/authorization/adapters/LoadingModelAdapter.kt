package com.example.authorization.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.authorization.Network.Network
import com.example.authorization.R
import com.example.authorization.databinding.CarItemBinding
import com.example.authorization.models.LoadingModel

import com.example.authorization.utils.Utils
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadingModelAdapter: RecyclerView.Adapter<LoadingModelAdapter.LoadingHolder>() {
    val car_list = ArrayList<LoadingModel.Car>()
    class LoadingHolder(item:View):RecyclerView.ViewHolder(item){
        val binding = CarItemBinding.bind(item)
        fun bind(car : LoadingModel.Car)= with(binding){

            num.text = car.number


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_item,parent,false)
        return LoadingHolder(view)
    }

    override fun getItemCount(): Int {
        return car_list.size
    }

    override fun onBindViewHolder(holder: LoadingHolder, position: Int) {
        holder.bind(car_list[position])
    }
    fun Refresh(type: Int){
        val call = Network.loadingApi.carsList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                //Если ответ успешен, получает тело ответа и передает его в функцию getCarsListJson()
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: ""
                    val cars = getLoadModelListJson(responseBody, type)
                    for (car in cars){

                        car_list.add(car)

                    }
                    notifyDataSetChanged()
                }

            }

            //ПриОшибке во время запроса. Вызывает continuation.resumeWithException(), передавая исключение Throwable.
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })

    }

    fun getLoadModelListJson(responseBody: String, type: Int): List<LoadingModel.Car>{
        val carList = mutableListOf<LoadingModel.Car>()
        val arrayJson = JSONArray(responseBody)
        for (item in 0 until arrayJson.length()){
            val objectJson = arrayJson.getJSONObject(item)
            val ref = objectJson.getString(Utils.Cars.REF_KEY)
            val name = when (type){
                Utils.ObjectModelType.CAR -> objectJson.getString(Utils.Cars.NUMBER_KEY)
                else -> objectJson.getString(Utils.Contracts.NAME_KEY)

            }
            val loadingObject = LoadingModel.Car(ref, name)

            carList.add(loadingObject)
        }
        return carList
    }

}