package com.example.authorization.utils

import com.example.authorization.models.AnyModel
import com.example.authorization.models.LoadingModel
import com.example.authorization.models.User

object Utils {
    var base_url = "http://192.168.168.121/TpbaseDemo/en_GB"
    var basename = "TpbaseDemo"
    var username = "Администратор"
    var password = "1"
    var loading_auth = "/hs/PogruzkaApi/"
    var auth = "/hs/TSD/"
    val acceptance_auth = "/hs/PriemkiAPI/"
    var lang = ""
    var clientTimeout = 10L
    var cars: List<LoadingModel> = listOf()
    var anyModel: AnyModel? = null
    var user = User()
    var productTypes: List<AnyModel.ProductType> = listOf()
    var addressess: List<AnyModel> = listOf()
    var packages: List<AnyModel> = listOf()
    var zones: List<AnyModel> = listOf()


    object Contracts {
        const val REF_KEY = "Ссылка"
        const val NAME_KEY = "Наименование"
        const val CODE_KEY = "Код"
    }

    //Объект с ключевыми названиями машин
    object Cars {
        const val REF_KEY = "GUID"
        const val NUMBER_KEY = "Номер"
    }

    object ObjectModelType {
        const val ADDRESS = 1
        const val _PACKAGE = 2
        const val PRODUCT_TYPE = 3
        const val ZONE = 4
        const val CAR = 5
        const val WAREHOUSE = 6
        const val EMPTY = 7
    }

    object OperationType {
        const val ACCEPTANCE = "ПриемГруза"
        const val WEIGHT = "ВзвешиваниеГруза"
        const val SIZE = "ЗамерГруза"
    }
}

