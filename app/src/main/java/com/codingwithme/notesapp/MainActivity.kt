package com.codingwithme.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class MainActivity : AppCompatActivity() {


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)


            val button = findViewById<Button>(R.id.button1)
            button.setOnClickListener {
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)
            }

        }
    }