package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var boton1: Button
    private lateinit var introduzcaNombre: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val fechaTextView = findViewById<TextView>(R.id.fecha)
        val dataActual = Date()
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaFormateada = formato.format(dataActual)

        fechaTextView.text = "La fecha es: $fechaFormateada"


        initComponent()
        initListeners()
        initUI()
    }

    private fun initComponent(){
        boton1 = findViewById<Button>(R.id.boton1)
        introduzcaNombre = findViewById<EditText>(R.id.introduceNombre)
    }

    private fun initListeners(){
        boton1.setOnClickListener {

            val nombre = introduzcaNombre.text.toString().trim()

            if(nombre.isEmpty()){
                introduzcaNombre.error = "Error, Introcueix un nom"
            } else {
                val intent = Intent(this, SegundaActivity::class.java )
                intent.putExtra("nombre", nombre)
                startActivity(intent)
            }
        }

    }
    private fun initUI() {
    }
}
