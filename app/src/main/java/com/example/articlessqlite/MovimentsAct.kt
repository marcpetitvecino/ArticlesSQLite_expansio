package com.example.articlessqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CursorAdapter
import android.widget.ListView

class MovimentsAct : AppCompatActivity() {

    private val context = this
    private var db = ArticlesRepository(context)

    private lateinit var listViewMoviments: ListView
    private lateinit var adapter: CursorAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moviments)

        var bundle: Bundle ?= intent.extras
        var dataInici = bundle!!.getString("dataInici")
        var dataFi = bundle!!.getString("dataFi")
        val codi = bundle.getString("codiArticle")

        listViewMoviments = findViewById(R.id.listViewMoviments)
        adapter = MovimentsAdapter(context, ArticlesRepository(context).getMovimentsCursor(dataInici!!, dataFi!!, codi!!))
        listViewMoviments.adapter = adapter
    }
}
