package com.codingwithme.notesapp.Remote

import com.codingwithme.notesapp.Model.APIResponse
import com.codingwithme.notesapp.Model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @POST("api/login")
    fun login( @Body user: User):Call<APIResponse>


}