package com.example.authorization.repository

import com.example.authorization.Network.Network
import com.example.authorization.models.LoadingModel
import com.example.authorization.models.User
import com.example.authorization.utils.Utils
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class BaseRepository {


    companion object{
         fun getRights(responseBody: String): Boolean {
            val jsonObject = JSONObject(responseBody)

            val isAdmin = jsonObject.optBoolean(IS_ADMIN, false)
            if (isAdmin){
                Utils.user = User(
                    username = Utils.username,
                    password = Utils.password,
                    isAdmin = true,
                    weightAccess = true,
                    measureCargo = true,
                    acceptanceCargo = true
                )
            } else {
                val acceptanceAdd = jsonObject.optBoolean(ACCEPTANCE_ADD, false)
                val weightAdd = jsonObject.optBoolean(WEIGHT_ADD, false)
                val sizeAdd = jsonObject.optBoolean(SIZE_ADD, false)
                Utils.user = User(
                    username = Utils.username,
                    password = Utils.password,
                    isAdmin = false,
                    weightAccess = weightAdd,
                    measureCargo = sizeAdd,
                    acceptanceCargo = acceptanceAdd
                )
            }

            return true
        }



        const val ACCEPTANCE_RIGHT = "PriemkiAccess"
        const val LOADING_RIGHT = "PogruzkiAccess"
        const val ACCEPTANCE_ADD= "PriemkiCargo"
        const val SIZE_ADD = "MeasureCargo"
        const val WEIGHT_ADD = "Weighing"
        const val IS_ADMIN = "ЭтоАдмин"
    }
}
