package com.codingwithme.notesapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
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

        val sharedPref : SharedPreferences = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE)
        val check = sharedPref.getBoolean(getString(R.string.check_login), false)

        if(check){
            val intent = Intent(this@login, MapsActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        }


    private fun authenticateUser(editTextUsername: String,editTextPassword: String){
        var user = User(editTextUsername, editTextPassword)
        //val buttonMudar2 = findViewById<Button>(R.id.buttonLogin)

       
        mService.login(user)
            .enqueue(object: Callback<APIResponse>{
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Toast.makeText(this@login,"Login Invalido",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {

                    if (response.isSuccessful){
                        Toast.makeText(this@login, getString(R.string.toast), Toast.LENGTH_SHORT).show()
                        val checkBox = findViewById<CheckBox>(R.id.checkBox)
                        val sharedPref : SharedPreferences = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE)
                        with(sharedPref.edit()){
                            putBoolean(getString(R.string.check_login), checkBox.isChecked)
                            putString(getString(R.string.id_user), response.body()?.id.toString())
                            commit()
                        }

                        Log.d("ze", response.body()!!.id)

                        val intent = Intent(this@login, MapsActivity::class.java)
                        intent.putExtra("id",  response.body()?.id.toString())
                        startActivity(intent)
                        finishAffinity()



                    }


                }

            })
        }
    }