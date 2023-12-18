package com.example.authorization.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.authorization.Network.Network
import com.example.authorization.R
import com.example.authorization.databinding.CarItemBinding
import com.example.authorization.databinding.SkladItemBinding
import com.example.authorization.models.LoadingModel

import com.example.authorization.utils.Utils
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SkladModelAdapter: RecyclerView.Adapter<SkladModelAdapter.SkladHolder>() {
    val sklads_list = ArrayList<LoadingModel.Warehouse>()
    class SkladHolder(item:View):RecyclerView.ViewHolder(item){
        val binding = SkladItemBinding.bind(item)
        fun bind(sklad : LoadingModel.Warehouse)= with(binding){

            name.text = sklad.name


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkladHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sklad_item,parent,false)
        return SkladHolder(view)
    }

    override fun getItemCount(): Int {
        return sklads_list.size
    }

    override fun onBindViewHolder(holder: SkladHolder, position: Int) {
        holder.bind(sklads_list[position])
    }
    fun Refresh(type: Int){
        val call = Network.loadingApi.warehouseList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                //Если ответ успешен, получает тело ответа и передает его в функцию getCarsListJson()
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: ""
                    val sklads = getLoadModelListJson(responseBody, type)
                    for (sklad in sklads){

                        sklads_list.add(sklad)

                    }
                    notifyDataSetChanged()
                }

            }

            //ПриОшибке во время запроса. Вызывает continuation.resumeWithException(), передавая исключение Throwable.
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })

    }

    fun getLoadModelListJson(responseBody: String, type: Int): List<LoadingModel.Warehouse>{
        val skladList = mutableListOf<LoadingModel.Warehouse>()
        val arrayJson = JSONArray(responseBody)
        for (item in 0 until arrayJson.length()){
            val objectJson = arrayJson.getJSONObject(item)
            val ref = objectJson.getString(Utils.Cars.REF_KEY)
            val name = when (type){
                Utils.ObjectModelType.CAR -> objectJson.getString(Utils.Cars.NUMBER_KEY)
                Utils.ObjectModelType.WAREHOUSE -> objectJson.getString(Utils.Contracts.NAME_KEY)
                else -> ""

            }
            val loadingObject = LoadingModel.Warehouse(ref, name)
//            when (type){
//                Utils.ObjectModelType.CAR -> LoadingModel.Car(ref, name)
//                Utils.ObjectModelType.WAREHOUSE -> LoadingModel.Warehouse(ref, name)
//                else -> LoadingModel.Car(ref, name)
//            }
            skladList.add(loadingObject)
        }
        return skladList
    }

}