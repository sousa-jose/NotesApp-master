package com.codingwithme.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codingwithme.notesapp.Common.Common
import com.codingwithme.notesapp.Model.APIResponse
import com.codingwithme.notesapp.Model.User
import com.codingwithme.notesapp.Remote.Api
import kotlinx.android.synthetic.main.login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class login : AppCompatActivity() {

    internal lateinit var mService: Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


        // iniciar o servico
        mService = Common.api



        buttonLogin.setOnClickListener{ authenticateUser(user.text.toString(), pass.text.toString())}

        val buttonMudar2 = findViewById<Button>(R.id.buttonLogin)

        buttonMudar2.setOnClickListener{

            val intent3 = Intent(this, MapsActivity::class.java)

            startActivity(intent3)
        }


        }


    private fun authenticateUser(editTextUsername: String,editTextPassword: String){
        var user = User(editTextUsername, editTextPassword)
       
        mService.login(user)
            .enqueue(object: Callback<APIResponse>{
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Toast.makeText(this@login,t!!.message,Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    //if(response!!.body()!!.error)
                  //  Toast.makeText(this@login,response.body()!!.error_msg,Toast.LENGTH_SHORT).show()
                   // else
                      Toast.makeText(this@login,"login efetuado com sucesso",Toast.LENGTH_SHORT).show()



                }

            })
        }
    }