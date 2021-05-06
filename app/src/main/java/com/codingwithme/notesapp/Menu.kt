package com.codingwithme.notesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.menu)

        val buttonMudar = findViewById<Button>(R.id.button3)
        val buttonChange = findViewById<Button>(R.id.botaoAceder)
        //val buttonChange2 = findViewById<Button>(R.id.buttonRegisto)


        buttonMudar.setOnClickListener{

            val intent = Intent(this, first_frag::class.java)

            startActivity(intent)
        }

        buttonChange.setOnClickListener{

            val intent9 = Intent(this, login::class.java)

            startActivity(intent9)
        }

        /*
        buttonChange2.setOnClickListener{

            val intent2 = Intent(this, Registo::class.java)

            startActivity(intent2)
        }*/

    }
}