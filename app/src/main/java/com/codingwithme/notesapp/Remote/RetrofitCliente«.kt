package com.codingwithme.notesapp.Remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object `RetrofitCliente«` {

    private var retrofit:Retrofit?=null
    private val client = OkHttpClient.Builder().build()

    var gson = GsonBuilder()
        .setLenient()
        .create();
    
            fun getClient(baseUrl:String):Retrofit{

                    retrofit = Retrofit.Builder()
                               .baseUrl(baseUrl)
                               .addConverterFactory(GsonConverterFactory.create(gson))
                               .client(client)
                               .build();


                return retrofit!!
            }

}