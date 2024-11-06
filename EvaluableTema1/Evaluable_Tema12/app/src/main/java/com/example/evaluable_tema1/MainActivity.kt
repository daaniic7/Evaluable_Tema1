package com.example.evaluable_tema1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    // Las diferentes variables correspondientes a los diferentes botones de la aplicación
    private lateinit var iv_finalSpace: ImageView
    private lateinit var iv_phone: ImageView
    private lateinit var iv_alarma: ImageView
    private lateinit var iv_extra: ImageView
    private lateinit var intent: Intent

    // El método onCreate para iniciar la aplicación por primera vez
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        iv_finalSpace = findViewById(R.id.iv6)
        iv_phone = findViewById(R.id.iv3)
        iv_alarma = findViewById(R.id.iv4)
        iv_extra = findViewById(R.id.iv_extra)

        // Botón para acceder al Activity TelefonoConfig
        iv_phone.setOnClickListener {
            intent = Intent(this, TelefonoConfig::class.java)
            startActivity(intent)
        }

        // Pulsando este botón se nos activará automaticamente una alarma que sonará
        // dos minutos después de la hora actual
        iv_alarma.setOnClickListener {
            alarma()
        }

        // Al pulsar este botón nos lleva a la página web que figura en el enlace
        iv_finalSpace.setOnClickListener {
            val url = "https://finalspaceends.com/"
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        // Al pulsar este botón serás redirigido a un video especifico de YouTube
        iv_extra.setOnClickListener {
            val url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    // Función para activar la alarma
    private fun alarma() {
        // Obtener la hora y minutos actuales
        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)

        // Sumar dos minutos a la hora actual
        minute += 2

        // Crear el Intent para configurar la alarma
        val intent_alarm = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Alarma en 2 minutos")
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minute)
        }
        startActivity(intent_alarm)
    }
}
