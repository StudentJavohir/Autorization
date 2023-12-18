package com.example.authorization.models

data class User(var username: String = "",
                var password: String = "",
                var isAdmin: Boolean = false,
                var weightAccess: Boolean = false,
                var measureCargo: Boolean = false,
                var acceptanceCargo: Boolean = false)
