
package com.codingwithme.notesapp.Remote

import com.codingwithme.notesapp.Model.APIResponse
import com.codingwithme.notesapp.Model.User
import com.codingwithme.notesapp.Model.modelReport
import com.codingwithme.notesapp.Model.reportOutput
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import okhttp3.MultipartBody


interface Api {

    @POST("api/login")
    fun login( @Body user: User):Call<APIResponse>

    @Multipart
    @POST("api/report")
    fun adicionarReport(
        @Part ficheiro: MultipartBody.Part,
        @Part("latitude") latitude:RequestBody,
        @Part("longitude") longitude:RequestBody,
        @Part("descricao") descricao:RequestBody,
        @Part("morada") morada:RequestBody,
        @Part("tipo") tipo:RequestBody,
        @Part("id_user") id_user:RequestBody
    ):Call<reportOutput>

    @GET("reports")
    fun pontosMapa():Call<List<modelReport>>

}