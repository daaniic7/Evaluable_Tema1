package com.example.evaluable_tema1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.evaluable_tema1.databinding.ActivityConfigTelefonoBinding

class TelefonoConfig : AppCompatActivity() {
    private lateinit var confBinding : ActivityConfigTelefonoBinding
    private lateinit var telefono_compartido : String
    private lateinit var fichero_compartido : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        confBinding = ActivityConfigTelefonoBinding.inflate(layoutInflater)
        setContentView(confBinding.root)


        PreferenciasCompartidas()
        start()

    }
    //Función que nos guarda el número de teléfono que hemos introducido
    private fun PreferenciasCompartidas(){
        val nombreFicheroCompartido = getString(R.string.preferencia_compartida)
        this.telefono_compartido = getString(R.string.numero_telefono)

        this.fichero_compartido = getSharedPreferences(nombreFicheroCompartido, MODE_PRIVATE)
    }

    private fun start() {

        confBinding.btEnviar.setOnClickListener{
            val numero_telefono = confBinding.editTextPhone.text.toString()

           //Si pulsamos el botón sin haber introducido un número de teléfono
            // Nos saldrá un mensaje pidiendonos que introduzcamos un número
            if (numero_telefono.isEmpty()){
                Toast.makeText(this, "Teclea un numero de telefono", Toast.LENGTH_LONG).show()
            }
            //Además tenemos otros dos casos más
            else{
                //Si el numero introducido no encaja con el formato de un numero
                //de españa nos lanzará un mensaje avisandonos sobre ello
                if (!NumeroEspanol(numero_telefono)){
                    Toast.makeText(this, "El formato introducido es incorrecto", Toast.LENGTH_LONG).show()
                }


                //Si se hace todo bien la aplicacion nos llevará al siguiente activity
                else{
                    val edit = fichero_compartido.edit()
                    edit.putString(telefono_compartido, numero_telefono)
                    edit.apply()
                    startMainActivity(numero_telefono)
                }
            }
        }
//Si pulsamos el botón atrás volvemos al mainActivity
        confBinding.btAtras.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("back", true)
            startActivity(intent)
        }
    }
//Método para comprobar que el número de teléfono introducido sigue el formato de
//un numero en españa
    private fun NumeroEspanol(numero_telefono: String): Boolean {

        val numeroLimpio = numero_telefono.replace(" ", "").replace("-", "")
        val regex = Regex("^[6789]\\d{8}$")
        return regex.matches(numeroLimpio)

    }

    private fun startMainActivity(numero_telefono: String) {
        val intent = Intent(this, Llamada::class.java)
        intent.apply {
            putExtra("phone", numero_telefono)
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        val ret = intent.getBooleanExtra("back", false)
        if (ret){
            confBinding.editTextPhone.setText("")
            Toast.makeText(this, "mensaje del numero de telefono nuevo", Toast.LENGTH_LONG)
            intent.removeExtra("back")
        }

    }
}