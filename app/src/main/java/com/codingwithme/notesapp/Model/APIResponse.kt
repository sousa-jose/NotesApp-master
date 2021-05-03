package com.codingwithme.notesapp.Model

data class APIResponse (
    var id_user: String,
    var email: String,
    var password: String,
    var address: Address
    )

data class Address(
    var street: String,
    var suite: String,
    var city: String,
    var zipcode: String,
    var geo: Geo
)

data class Geo(
    var lat: String,
    var lng: String
)