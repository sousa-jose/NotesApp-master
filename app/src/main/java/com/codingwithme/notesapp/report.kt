package com.codingwithme.notesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codingwithme.notesapp.Common.Common
import com.codingwithme.notesapp.Model.APIResponse
import com.codingwithme.notesapp.Model.User
import com.codingwithme.notesapp.Model.modelReport
import com.codingwithme.notesapp.Remote.Api
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class report: AppCompatActivity() {

    internal lateinit var mService: Api

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.report)
        mService = Common.api

        val reportar = findViewById<Button>(R.id.botaoRepor)




        val sharedpreferences1 = getSharedPreferences("localizacao", Context.MODE_PRIVATE)
        val longitude = sharedpreferences1.getString("longitude", "100")
        val latitude= sharedpreferences1.getString("latitude", "100")
        val address = sharedpreferences1.getString("morada", "Rua candido de olivera")

        reportar.setOnClickListener {
           // val modelReport = modelReport(id_report = "", latitude = latitude!!, longitude = longitude!!, descricao = "fgcvhbjh", morada = address!!)
            adicionarReport(id_report = "", latitude = latitude!!, longitude = longitude!!, descricao = "", morada = address!!)
        }

        findViewById<TextView>(R.id.morada).text = address

    }

    private fun adicionarReport(id_report: String, latitude:String, longitude: String, descricao:String, morada: String){

        var repo = modelReport(id_report,latitude,longitude,descricao,morada)



        mService.adicionarReport(repo)
            .enqueue(object: Callback<modelReport> {
                override fun onFailure(call: Call<modelReport>, t: Throwable) {
                    Toast.makeText(this@report,"tenta de novo", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<modelReport>, response: Response<modelReport>) {
                    //if(response!!.body()!!.error)
                    //  Toast.makeText(this@login,response.body()!!.error_msg,Toast.LENGTH_SHORT).show()
                    // else
             
                    Toast.makeText(this@report,"ainda bem q deste", Toast.LENGTH_SHORT).show()


                }

            })
    }


}