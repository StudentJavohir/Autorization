package com.example.authorization


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authorization.Network.Network
import com.example.authorization.adapters.AcceptanceModelAdapter
import com.example.authorization.adapters.LoadingModelAdapter
import com.example.authorization.adapters.SkladModelAdapter
import com.example.authorization.databinding.ActivityMainBinding
import com.example.authorization.databinding.CarsBinding
import com.example.authorization.databinding.DocsBinding
import com.example.authorization.databinding.ItemAcceptanceBinding
import com.example.authorization.databinding.NavigationBinding
import com.example.authorization.databinding.SkladsBinding
import com.example.authorization.models.AnyModel

import com.example.authorization.utils.Utils
import com.example.authorization.repository.BaseRepository
import okhttp3.ResponseBody
import org.json.JSONArray


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var types_list = ArrayList<AnyModel.ProductType>()
    var package_list = ArrayList<AnyModel.PackageModel>()
    private lateinit var cars_binding: CarsBinding
    private lateinit var itemAcceptance: ItemAcceptanceBinding
    private lateinit var navigationBinding: NavigationBinding
    private lateinit var docsBinding: DocsBinding
    private lateinit var skladsBinding: SkladsBinding
    private val adapter = LoadingModelAdapter()
    private val skladAdapter = SkladModelAdapter()

    private val acceptanceAdapter = AcceptanceModelAdapter{acceptance ->
        itemAcceptance = ItemAcceptanceBinding.inflate(layoutInflater)
        itemAcceptance.client.text = acceptance.client
        itemAcceptance.numDoc.text = acceptance.number

        val packageType = package_list.find {
            (it as? AnyModel.PackageModel)?.ref == acceptance.packageUid
        } as? AnyModel.PackageModel

        itemAcceptance.typeContainer.text = packageType!!.name

        val productType = types_list.find {
            (it as? AnyModel.ProductType)?.ref == acceptance.productType
        } as? AnyModel.ProductType

        itemAcceptance.typeProduct.text = productType!!.name
        itemAcceptance.quantityPlaces.text = acceptance.countSeat.toString()
        itemAcceptance.backToList.setOnClickListener(){
            setContentView(docsBinding.root)
        }
        setContentView(itemAcceptance.root)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        cars_binding = CarsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GetProductTypes()
        GetTypesUpakovok()
        binding.button.setOnClickListener(){

            Auth()

        }



    }

    fun ShowOption(){
        navigationBinding = NavigationBinding.inflate(layoutInflater)

        navigationBinding.apply {
            setContentView(navigationBinding.root)
            cars.setOnClickListener {
                InitCarList()
            }
            sklads.setOnClickListener {
                InitSkladList()
            }
            docs.setOnClickListener {
                InitAcceptanceList()
            }
        }
    }


    fun Auth(){

        Utils.username = binding.edtxUsername.text.toString()
        Utils.password = binding.edtxPassword.text.toString()

        val call = Network.Api.auto()

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val body = response.body()?.string() ?: ""
                    BaseRepository.getRights(body)

                    binding.textView.text = "Successful"
                    //InitCarList()
                    ShowOption()

                    return
                }else if (response.raw().code ==401){
                    binding.textView.text = "Authorization is failed"
                } else {
                    binding.textView.text = "Fail"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                binding.textView.text = "Fail with throw"
            }
        })
    }

    fun GetProductTypes(){

        val call = Network.api.productTypeList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                //Если ответ успешен, получает тело ответа и передает его в функцию getCarsListJson()
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: ""
                    val types = getAddressListJson(responseBody)
                    for(type in types){
                        types_list.add(type)
                    }

                }


            }

            //ПриОшибке во время запроса. Вызывает continuation.resumeWithException(), передавая исключение Throwable.
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })

    }
    private fun getAddressListJson(responseBody: String): List<AnyModel.ProductType> {
        val addressList = mutableListOf<AnyModel.ProductType>()
        val arrayJson = JSONArray(responseBody)
        for (item in 0 until arrayJson.length()) {
            val objectJson = arrayJson.getJSONObject(item)
            val ref = objectJson.getString(Utils.Contracts.REF_KEY)
            val name =   objectJson.getString(Utils.Contracts.NAME_KEY)



            val code = objectJson.getString(Utils.Contracts.CODE_KEY)
            val anyObject =  AnyModel.ProductType(ref, name, code)

            addressList.add(anyObject)
        }

        return addressList
    }
    fun GetTypesUpakovok(){

        val call = Network.api.packageList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                //Если ответ успешен, получает тело ответа и передает его в функцию getCarsListJson()
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: ""
                    val types = getAddressPackJson(responseBody)
                    for(type in types){
                        package_list.add(type)
                    }

                }


            }

            //ПриОшибке во время запроса. Вызывает continuation.resumeWithException(), передавая исключение Throwable.
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })

    }
    private fun getAddressPackJson(responseBody: String): List<AnyModel.PackageModel> {
        val addressList = mutableListOf<AnyModel.PackageModel>()
        val arrayJson = JSONArray(responseBody)
        for (item in 0 until arrayJson.length()) {
            val objectJson = arrayJson.getJSONObject(item)
            val ref = objectJson.getString(Utils.Contracts.REF_KEY)
            val name =   objectJson.getString(Utils.Contracts.NAME_KEY)



            val code = objectJson.getString(Utils.Contracts.CODE_KEY)
            val anyObject =  AnyModel.PackageModel(ref, name, code)

            addressList.add(anyObject)
        }

        return addressList
    }

    fun InitCarList(){
        cars_binding = CarsBinding.inflate(layoutInflater)
        cars_binding.apply {
            setContentView(cars_binding.root)
            Cars.layoutManager = LinearLayoutManager(this@MainActivity)
            Cars.adapter = adapter
            refresh.setOnClickListener {
                adapter.Refresh(Utils.ObjectModelType.CAR)
            }
            back.setOnClickListener {
                setContentView(navigationBinding.root)
            }
        }
    }
    fun InitSkladList(){
        skladsBinding = SkladsBinding.inflate(layoutInflater)
        skladsBinding.apply {
            setContentView(skladsBinding.root)
            Sklads.layoutManager = LinearLayoutManager(this@MainActivity)
            Sklads.adapter = skladAdapter
            refresh.setOnClickListener {
                skladAdapter.Refresh(Utils.ObjectModelType.WAREHOUSE)
            }
            back.setOnClickListener {
                setContentView(navigationBinding.root)
            }
        }
    }
    fun InitAcceptanceList(){
        docsBinding = DocsBinding.inflate(layoutInflater)
        docsBinding.apply {
            setContentView(docsBinding.root)
            Docs.layoutManager = LinearLayoutManager(this@MainActivity)
            Docs.adapter = acceptanceAdapter
            refresh.setOnClickListener {
                acceptanceAdapter.Refresh()
            }
            back.setOnClickListener {
                setContentView(navigationBinding.root)
            }
        }
    }


}