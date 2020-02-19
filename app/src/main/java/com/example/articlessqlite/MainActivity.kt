package com.example.articlessqlite

import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.introduir_codi.view.*
import kotlinx.android.synthetic.main.modify_stock_alert.view.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val context = this
    private var db = ArticlesRepository(context)

    private lateinit var listView: ListView
    private lateinit var adapter: CursorAdapter
    private lateinit var searchView: SearchView
    private lateinit var stockView: TextView

    override fun onStart() {
        super.onStart()
        if (!adapter.isEmpty) {

            adapter.notifyDataSetChanged()

        }

    }

    override fun onResume() {
        super.onResume()

        var c: Cursor? = null
        c = ArticlesRepository(context).getDataCursor()


        if (c.moveToFirst()) {

            adapter.changeCursor(c)
            adapter.notifyDataSetChanged()

        }



    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listViewArticles)

        adapter = ArticleAdapter(context, ArticlesRepository(context).getDataCursor())

        listView.adapter = adapter


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        searchView = menu!!.findItem(R.id.searchView).actionView as SearchView

        initListeners()

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.afegirArticleBtn -> {

                val intent = Intent(this, InsertDataAct::class.java).apply {}
                startActivity(intent)

            }

            R.id.obrirWeather -> {

                val intent = Intent(this, WeatherAct::class.java).apply {}
                startActivity(intent)
            }

            R.id.obrirIntervalMoviments -> {

                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                var intent: Intent? = null

                var dataInici: String? = null
                var dataFi: String? = null

                val dialogView = LayoutInflater.from(context).inflate(R.layout.introduir_codi, null)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Introdueix el codi de l'article: ")
                builder.setView(dialogView)
                builder.setPositiveButton("Ok") {dialog, which ->

                    val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        dataInici = "$year-${monthOfYear+1}-$dayOfMonth"

                        val dpd2 = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                            dataFi =  "$year-${monthOfYear+1}-$dayOfMonth"

                            intent = Intent(context, MovimentsAct()::class.java)
                            intent!!.putExtra("dataInici", dataInici)
                            intent!!.putExtra("dataFi", dataFi)
                            intent!!.putExtra("codiArticle", dialogView.alertIntroduirCodi.text.toString())
                            startActivity(intent)

                        }, year, month, day)

                        dpd2.show()
                    }, year, month, day)

                    dpd.show()



                }

                builder.setNegativeButton("Cancelar") {dialog, which ->}

                val dialog = builder.create()
                dialog.show()

            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun initListeners() {

        listView.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(this, DetailAct::class.java)
            val article = adapter.cursor
            article.moveToPosition(position)
            val idArticle = article.getString(article.getColumnIndex("_id"))

            intent.putExtra("id", idArticle.toInt())
            startActivity(intent)


        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null) {

                    val queryCursor = ArticlesRepository(context).searchByDescription(newText)
                    listView.adapter = ArticleAdapter(context, queryCursor)
                    adapter.notifyDataSetChanged()

                }

                return true

            }


        })


    }

}
