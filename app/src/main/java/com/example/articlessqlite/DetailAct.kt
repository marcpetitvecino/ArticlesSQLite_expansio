package com.example.articlessqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import org.w3c.dom.Text

class DetailAct : AppCompatActivity() {

    val context = this
    private lateinit var detailCode: TextView
    private lateinit var detailDescription: EditText
    private lateinit var detailPVP: EditText
    private lateinit var detailStock: TextView
    private lateinit var buttonModificar: Button
    private lateinit var buttonEsborrar: Button
    private var idArticle: Int = 0
    private lateinit var article: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViews()

        val intent = intent
        var bundle: Bundle ?= intent.extras
        var id = bundle!!.getInt("id")

        idArticle = id

        article = ArticlesRepository(context).findArticleById(idArticle)

        loadData(article)
        initListeners()

    }

    private fun initViews() {

        detailCode = findViewById(R.id.detailCode)
        detailDescription = findViewById(R.id.detailDesc)
        detailPVP = findViewById(R.id.detailPVP)
        detailStock = findViewById(R.id.detailStock)
        buttonModificar = findViewById(R.id.botoModificar)
        buttonEsborrar = findViewById(R.id.botoEsborrar)

    }

    private fun loadData(article: Article) {

        detailCode.text = article.getCode()
        detailDescription.setText(article.getDescripcio())
        detailPVP.setText(article.getPVP().toString())
        detailStock.setText(article.getStock().toString())

    }

    private fun initListeners() {

        buttonModificar.setOnClickListener {

            if (detailStock.text.toString() == "") detailStock.setText("0")
            if (detailPVP.text.toString() == "") detailPVP.setText("0.0")

            ArticlesRepository(context).updateArticle(idArticle, detailDescription.text.toString(), detailStock.text.toString().toInt(), detailPVP.text.toString().toDouble())
            val intent = Intent(this, MainActivity::class.java).apply {}
            startActivity(intent)
        }

        buttonEsborrar.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Alerta!")
            builder.setMessage("Segur que vols esborrar l'article?")

            builder.setPositiveButton("Si") { dialog, which ->

                ArticlesRepository(context).esborrarArticle(idArticle)
                val intent = Intent(this, MainActivity::class.java).apply {}
                startActivity(intent)

            }

            builder.setNegativeButton("No") { dialog, which ->
            }

            val dialog = builder.create()
            dialog.show()


        }

    }

}
