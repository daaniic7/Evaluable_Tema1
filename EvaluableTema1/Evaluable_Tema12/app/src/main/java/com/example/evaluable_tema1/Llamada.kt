package com.example.evaluable_tema1

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.evaluable_tema1.databinding.ActivityLlamadaBinding

class Llamada : AppCompatActivity() {
    private lateinit var confBinding: ActivityLlamadaBinding
    private lateinit var telefono_compartido: String
    private lateinit var fichero_compartido: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        confBinding = ActivityLlamadaBinding.inflate(layoutInflater)
        setContentView(confBinding.root)

        PreferenciasCompartidas()

        confBinding.btLlamar.setOnClickListener {
            pedirPermisos()
        }
        confBinding.ivAtras.setOnClickListener {
            intent = Intent(this, TelefonoConfig::class.java)
            Toast.makeText(this, "Introduce un número de teléfono", Toast.LENGTH_LONG).show()
            startActivity(intent)
        }
        mostrarNumeroTelefono()


    }

    private fun PreferenciasCompartidas() {
        val nombreFicheroCompartido = getString(R.string.preferencia_compartida)
        this.telefono_compartido = getString(R.string.numero_telefono)

        this.fichero_compartido = getSharedPreferences(nombreFicheroCompartido, MODE_PRIVATE)

    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
            isGranted ->
        if (isGranted) {
            call()

        } else {
            Toast.makeText(
                this, "Debes habilitar los permisos",
                Toast.LENGTH_LONG
            ).show()
            goToConfiguration()

        }
    }

//Comprueba si los permisos del teléfono están activados o no para realizar la llamada
    private fun permisosDelTelefono(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED

//Se nos solicitan los permisos para hacer llamadas
    private fun pedirPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Se verificas si el SDK es mayor que 23
            if (permisosDelTelefono()) { // Si los permisos ya están concedidos
                call() // Realiza la llamada
            } else { // Solicitar permisos
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)

            }
        } else {
            call() // No es necesario solicitar permisos, ya que es una API < 23
        }
    }

//Función que realiza la llamada al número  que hemos establecido si los permisos están activados
    private fun call() {
        val numeroTelefonoCompartido =
            fichero_compartido.getString(getString(R.string.numero_telefono), "") ?: ""
        val intent = Intent(Intent.ACTION_CALL).apply {
            data =
                Uri.parse("tel:${numeroTelefonoCompartido.toString()}")
        }
        startActivity(intent)
    }
//Función que muestra el número de teléfono que hemos puesto en el anterior activity
    private fun mostrarNumeroTelefono() {
        // Recupera el número de teléfono desde SharedPreferences
        val numeroTelefono =
            fichero_compartido.getString(getString(R.string.numero_telefono), "Número")

        // Asigna el número al TextView
        confBinding.textView.text = numeroTelefono
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
//Función que nos redirije a la información de la app dentro de la configuración del teléfono
    //para que activemos los permisos desde ahi
    private fun goToConfiguration() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null);
        }
        startActivity(intent)
    }
}
