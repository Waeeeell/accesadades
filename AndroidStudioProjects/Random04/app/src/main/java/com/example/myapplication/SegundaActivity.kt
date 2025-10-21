package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SegundaActivity : AppCompatActivity() {

    private lateinit var volver: Button
    private lateinit var numeroIntroducido: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_segunda)

        val nombre: String = intent.extras?.getString("nombre")?: "Ningún nombre introducido"
        val nombreMotrar = findViewById<TextView>(R.id.mostrarNombre)

        nombreMotrar.text = "Perfecto, $nombre, te toca adivinar un número entre el 1 y el 3"

        initComponent()
        initListener()
        comprovaNumero()
    }

    private fun initComponent(){
        volver = findViewById<Button>(R.id.volver)
        numeroIntroducido = findViewById<EditText>(R.id.introduceNombre)

    }

    private fun initListener(){

        volver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }
    }

    private fun comprovaNumero(){


    }
}