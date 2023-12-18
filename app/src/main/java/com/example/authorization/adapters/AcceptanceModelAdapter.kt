package com.example.authorization.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.authorization.Network.Network
import com.example.authorization.R
import com.example.authorization.databinding.CarItemBinding
import com.example.authorization.databinding.DocItemBinding
import com.example.authorization.models.Acceptance
import com.example.authorization.models.AnyModel
import com.example.authorization.models.LoadingModel

import com.example.authorization.utils.Utils
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AcceptanceModelAdapter(private val onItemClicked: (acceptance: Acceptance) -> Unit): RecyclerView.Adapter<AcceptanceModelAdapter.AcceptanceHolder>() {
    val acceptance_list = ArrayList<Acceptance>()

    inner class AcceptanceHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = DocItemBinding.bind(item)

        init {
            // Set click listener for the item
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val acceptance = acceptance_list[position]
                    onItemClicked(acceptance)
                }
            }
        }

        fun bind(acceptance: Acceptance) = with(binding) {
            client.text = acceptance.client
            num.text = acceptance.number
            date.text = acceptance.date
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptanceHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.doc_item,parent,false)
        return AcceptanceHolder(view)
    }

    override fun getItemCount(): Int {
        return acceptance_list.size
    }

    override fun onBindViewHolder(holder: AcceptanceHolder, position: Int) {
        holder.bind(acceptance_list[position])
    }
    fun Refresh(){
        val call = Network.api.acceptanceList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                //Если ответ успешен, получает тело ответа и передает его в функцию getCarsListJson()
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: ""
                    val docs = getAcceptanceList(responseBody)

                    for (doc in docs){

                        acceptance_list.add(doc)

                    }
                    notifyDataSetChanged()
                }

            }

            //ПриОшибке во время запроса. Вызывает continuation.resumeWithException(), передавая исключение Throwable.
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })

    }



    private fun getAcceptanceList(responseString: String): List<Acceptance> {
        val acceptanceList = mutableListOf<Acceptance>()
        val acceptArray = JSONArray(responseString)
        for (index in 0 until acceptArray.length()) {
            val acceptJson = acceptArray.getJSONObject(index)
            val acceptance = getaAcceptanceFromJsonObject(acceptJson,true)
            acceptanceList.add(acceptance)
        }
        return acceptanceList
    }


    private fun getaAcceptanceFromJsonObject(
        acceptJson: JSONObject,
        hasAdditionalFields: Boolean = false,
    ): Acceptance {
        val ref = acceptJson.getString(REF_KEY)
        val date = acceptJson.getString(DATE_KEY)
        val number = acceptJson.getString(NUMBER_KEY)
        val client = acceptJson.getString(CLIENT_KEY)
        val packageUid = acceptJson.getString(PACKAGE_UID_KEY)
        val packageName = getPackageNameFromUid(packageUid)
        val zoneUid = acceptJson.getString(ZONE_KEY)
        val zoneName = getZoneNameFromUid(zoneUid)
        val weight = acceptJson.getBoolean(WEIGHT_KEY)
        val capacity = acceptJson.getBoolean(CAPACITY_KEY)
        if (hasAdditionalFields) {
//            val autoNumber = acceptJson.optString(AUTO_NUMBER_KEY, "")
//            val idCard = acceptJson.getString(ID_CARD_KEY)
//            val countSeat = acceptJson.getInt(COUNT_SEAT_KEY)
//            val countInPackage = acceptJson.getInt(COUNT_IN_PACKAGE_KEY)
//            val countPackage = acceptJson.getInt(COUNT_PACKAGE_KEY)
//            val allWeight = acceptJson.getDouble(ALL_WEIGHT_KEY)
//            val storeUid = acceptJson.getString(STORE_UID_KEY)
//            val storeAddressName = getAddressNameFromUid(storeUid)
            val productType = acceptJson.getString(PRODUCT_TYPE_KEY)
            val productTypeName = getProductTypeFromUid(productType)
//            val phoneNumber = acceptJson.getString(PHONE_KEY)
//            val storeName = acceptJson.getString(STORE_NAME_KEY)
//            val batchGuid = acceptJson.getString(BATCH_GUID_KEY)
//            val representativeName = acceptJson.getString(REPRESENTATIVE_NAME_KEY)
//            val z = acceptJson.getBoolean(Z_KEY)
//            val brand = acceptJson.getBoolean(BRAND_KEY)
//            val glass = acceptJson.getBoolean(GLASS_KEY)
//            val expensive = acceptJson.getBoolean(EXPENSIVE_KEY)
//            val isPrinted = acceptJson.getBoolean(PRINTED_KEY)
//            val notTurnOver = acceptJson.getBoolean(NOT_TURN_OVER_KEY)
//            val whoAccept = acceptJson.getString(WHO_ACCEPT_KEY)
//            val whoWeigh = acceptJson.getString(WHO_WEIGH_KEY)
//            val whoMeasure = acceptJson.getString(WHO_MEASURE_KEY)
            return Acceptance(
//                z = z,
//                brand = brand,
//                glass = glass,
//                expensive = expensive,
//                notTurnOver = notTurnOver,
                ref = ref,
                date = date,
//                countPackage = countPackage,
//                storeAddressName = storeAddressName,
                productTypeName = productTypeName,
//                batchGuid = batchGuid,
//                autoNumber = autoNumber,
//                countInPackage = countInPackage,
                number = number,
//                phoneNumber = phoneNumber,
                packageUid = packageUid,
//                storeName = storeName,
                productType = productType,
//                countSeat = countSeat,
//                storeUid = storeUid,
//                idCard = idCard,
                zone = zoneName,
                _package = packageName,
//                allWeight = allWeight,
                client = client,
                zoneUid = zoneUid,
//                representativeName = representativeName,
//                isPrinted = isPrinted,
                weight = weight,
                capacity = capacity,
//                whoAccept = whoAccept,
//                whoMeasure = whoMeasure,
//                whoWeigh = whoWeigh
            )
        }
        return Acceptance(
            _package = packageName,
            zoneUid = zoneUid,
            zone = zoneName,
            packageUid = packageUid,
            ref = ref,
            number = number,
            client = client,
            weight = weight,
            date = date,
            capacity = capacity
        )
    }
    private fun getZoneNameFromUid(zoneUid: String): String {
        val elem = Utils.zones.find {
            (it as AnyModel.Zone).ref == zoneUid
        } ?: return ""
        return (elem as AnyModel.Zone).name
    }

    private fun getProductTypeFromUid(zoneUid: String): String {
        val elem = Utils.productTypes.find {
            (it as AnyModel.ProductType).ref == zoneUid
        } ?: return ""
        return (elem as AnyModel.ProductType).name
    }

    private fun getPackageNameFromUid(zoneUid: String): String {
        val elem = Utils.packages.find {
            (it as AnyModel.PackageModel).ref == zoneUid
        } ?: return ""
        return (elem as AnyModel.PackageModel).name
    }

    private fun getAddressNameFromUid(zoneUid: String): String {
        val elem = Utils.addressess.find {
            (it as AnyModel.AddressModel).ref == zoneUid
        } ?: return ""
        return (elem as AnyModel.AddressModel).name
    }

    companion object {
        const val REF_KEY = "Ссылка"
        const val DATE_KEY = "Дата"
        const val NUMBER_KEY = "Номер"
        const val CLIENT_KEY = "Клиент"
        const val AUTO_NUMBER_KEY = "НомерАвто"
        const val ID_CARD_KEY = "IDПродавца"
        const val WEIGHT_KEY = "Вес"
        const val CAPACITY_KEY = "Замер"
        const val ZONE_KEY = "Зона"
        const val COUNT_SEAT_KEY = "КоличествоМест"
        const val COUNT_IN_PACKAGE_KEY = "КоличествоВУпаковке"
        const val COUNT_PACKAGE_KEY = "КоличествоТиповУпаковок"
        const val ALL_WEIGHT_KEY = "ОбщийВес"
        const val PACKAGE_UID_KEY = "ТипУпаковки"
        const val PRODUCT_TYPE_KEY = "ВидТовара"
        const val STORE_UID_KEY = "АдресМагазина"
        const val PHONE_KEY = "ТелефонМагазина"
        const val STORE_NAME_KEY = "НаименованиеМагазина"
        const val REPRESENTATIVE_NAME_KEY = "ИмяПредставителя"
        const val BATCH_GUID_KEY = "GUIDПартии"
        const val Z_KEY = "ZТовар"
        const val BRAND_KEY = "Брэнд"
        const val GLASS_KEY = "Стекло"
        const val EXPENSIVE_KEY = "Дорогой"
        const val NOT_TURN_OVER_KEY = "НеКантовать"
        const val PRINTED_KEY = "Напечатан"

        const val READ_ONLY = "ViewOnly"
        const val WEIGHT_ENABLE = "InputWeight"
        const val SIZE_ENABLE = "InputSizes"
        const val IS_CREATOR = "OwnDocument"
        const val PROPS_ENABLE = "PropertiesProducts"
        const val ZONE_ENABLE = "Zona"
        const val CH_BOX_ENABLE = "Сheckbox"
        const val PACKAGE_ENABLE = "AmountInPackage"

        const val WHO_ACCEPT_KEY = "ТоварПринял"
        const val WHO_WEIGH_KEY = "ТоварВзвесил"
        const val WHO_MEASURE_KEY = "ТоварИзмерил"
        const val CREATOR_KEY = "Создатель"
        const val TYPE_KEY = "Тип"
        const val ERROR_ARRAY = "ПричинаОшибки"
        const val FIELDS_PROPERTY_KEY = "ПараметрыВидимости"
        const val ON_CHINESE = "НаКитайском"
        const val NEXT_IS_NEED_KEY = "НуженСледующий"
        const val RESULT_KEY = "Результат"
        const val ERROR_REASON_KEY = "ПричинаОшибки"
    }


}