package com.codingwithme.notesapp.Common

import com.codingwithme.notesapp.Remote.Api
import com.codingwithme.notesapp.Remote.`RetrofitCliente«`

object Common {

    val BASE_URL="https://qwertqq.000webhostapp.com/myslim/users/"

    val api:Api
    get() = `RetrofitCliente«`.getClient(BASE_URL).create(Api::class.java)
}