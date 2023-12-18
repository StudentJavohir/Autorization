package com.example.authorization.models

sealed class LoadingModel{
    data class Car(
        var ref: String, // Ссылка
        var number: String //Номер
    ) : LoadingModel()

    data class Warehouse(
        var ref: String, // Ссылка
        var name: String //Наименование
    ) : LoadingModel()
}
