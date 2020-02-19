package com.example.articlessqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class InsertDataAct : AppCompatActivity() {

    var context = this
    private lateinit var insertCodi: EditText
    private lateinit var insertDescripcio: EditText
    private lateinit var insertPVP: EditText
    private lateinit var insertStock: EditText
    private lateinit var buttonAfegir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_data)

        initViews()
        initListeners()

    }

    private fun initViews() {

        insertCodi = findViewById(R.id.insertarCodi)
        insertDescripcio = findViewById(R.id.insertarDescripcio)
        insertPVP = findViewById(R.id.insertarPVP)
        insertStock = findViewById(R.id.insertarStock)
        buttonAfegir = findViewById(R.id.botoInserir)

    }

    private fun initListeners() {

        buttonAfegir.setOnClickListener {

            if (insertStock.text.toString() == "") insertStock.setText("0")
            if (insertPVP.text.toString() == "") insertPVP.setText("0.0")


                if (checkInsert(insertCodi.text.toString(), insertDescripcio.text.toString(), insertStock.text.toString().toInt())) {

                    ArticlesRepository(context).insertarArticle(insertCodi.text.toString(), insertDescripcio.text.toString(), insertPVP.text.toString().toDouble(), insertStock.text.toString().toInt())
                    val intent = Intent(this, MainActivity::class.java).apply {}
                    startActivity(intent)

                }



        }

    }

    private fun checkInsert(codi: String, descripcio: String, stock:Int): Boolean {

        if (!ArticlesRepository(context).checkIfCodeExists(codi)) {

            Toast.makeText(context, "El codi ja existeix a la BDD", Toast.LENGTH_SHORT).show()

        }

        if (descripcio.isEmpty()) {

            Toast.makeText(context, "La descripcio no pot estar en blanc", Toast.LENGTH_SHORT).show()

        }

        if (stock < 0) {

            Toast.makeText(context, "El stock no pot ser inferior a 0", Toast.LENGTH_SHORT).show()

        }

        if (ArticlesRepository(context).checkIfCodeExists(codi) && !descripcio.isEmpty() && stock >= 0) return true

        return false

    }

}
