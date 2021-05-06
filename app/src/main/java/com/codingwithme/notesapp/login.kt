package com.codingwithme.notesapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

      buttonLogin.setOnClickListener{ authenticateUser(user2.text.toString(), pass.text.toString())}

        }


    private fun authenticateUser(editTextUsername: String,editTextPassword: String){
        var user = User(editTextUsername, editTextPassword)
        val buttonMudar2 = findViewById<Button>(R.id.buttonLogin)

       
        mService.login(user)
            .enqueue(object: Callback<APIResponse>{
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Toast.makeText(this@login,"Login Incorreto!",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    val Sharedpreferences2 = getSharedPreferences("login", Context.MODE_PRIVATE)
                    val isRemembered = Sharedpreferences2.getBoolean("checkbox",false)
                    //if(response!!.body()!!.error)
                  //  Toast.makeText(this@login,response.body()!!.error_msg,Toast.LENGTH_SHORT).show()
                   // else
                     //Toast.makeText(this@login,"login efetuado com sucesso",Toast.LENGTH_SHORT).show()

                    if(isRemembered){
                        val intent3 = Intent(this@login, MapsActivity::class.java)

                        startActivity(intent3)
                    }

                    buttonMudar2.setOnClickListener{


                        val mail : String = user2.text.toString()
                        val passe : String = pass.text.toString()
                        val checked : Boolean = checkBox.isChecked

                        val editor: SharedPreferences.Editor = Sharedpreferences2.edit()
                        editor.putString("email", mail)
                        editor.putString("palavrapasse", passe)
                        editor.putBoolean("check", checked)
                        editor.apply()

                        Toast.makeText(this@login,"guardado",Toast.LENGTH_SHORT).show()

                        val intent3 = Intent(this@login, MapsActivity::class.java)

                        startActivity(intent3)


                    }


                }

            })
        }
    }