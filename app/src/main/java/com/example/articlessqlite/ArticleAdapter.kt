package com.example.articlessqlite

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.modify_stock_alert.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class ArticleAdapter(val context: Context, cursor:Cursor): CursorAdapter(context, cursor, true) {

    private lateinit var codiView: TextView
    private lateinit var descripcioView: TextView
    private lateinit var stockView: TextView
    private lateinit var cellView: LinearLayout

    override fun newView(p0: Context?, p1: Cursor?, p2: ViewGroup?): View {

        return LayoutInflater.from(context).inflate(R.layout.article_cell, p2, false)
    }

    override fun bindView(p0: View?, p1: Context?, p2: Cursor?) {

        codiView = p0?.findViewById(R.id.codiArticle)!!
        descripcioView = p0.findViewById(R.id.DescripcioArticle)
        stockView = p0.findViewById(R.id.stockArticle)
        cellView = p0.findViewById(R.id.cellArticle)

        val buttonAdd = p0.findViewById<TextView>(R.id.botoAfegirStock)
        val buttonRestar = p0.findViewById<TextView>(R.id.botoReduirStock)

        val codi = cursor!!.getString(cursor.getColumnIndex(COL_CODE))
        val descripcio = cursor!!.getString(cursor.getColumnIndex(COL_DESCRIPCIO))
        val stock = cursor!!.getString(cursor.getColumnIndex(COL_STOCK))
        codiView!!.text = "Codi: $codi"
        descripcioView!!.text = "Descripcio: $descripcio"
        stockView!!.text = "Stock: $stock"


        if (stock.toString().toInt() <= 0) {

            cellView!!.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light))

        } else {

            cellView!!.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))

        }

        buttonAdd!!.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            var fullDay: String? = null

            val dialogView = LayoutInflater.from(context).inflate(R.layout.modify_stock_alert, null)


            val builder = AlertDialog.Builder(context)
            builder.setTitle("Afegir stock")
            builder.setView(dialogView)
            builder.setPositiveButton("Ok") {dialog, which ->

                if (dialogView.alertModifyStock.text.toString() == "") {

                    dialogView.alertModifyStock.setText("0")

                }

                val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    fullDay = "$year/${monthOfYear+1}/$dayOfMonth"
                    ArticlesRepository(context).modificarStock(dialogView.alertModifyStock.text.toString().toInt(), cursor.getInt(cursor.getColumnIndex("_id")), fullDay!!,"E")
                    val intent = Intent(context, MainActivity()::class.java)
                    context.startActivity(intent)

                }, year, month, day)

                dpd.show()

            }

            builder.setNegativeButton("Cancelar") {dialog, which ->}

            val dialog = builder.create()
            dialog.show()

        }

        buttonRestar!!.setOnClickListener {


            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            var fullDay: String? = null

            val dialogView = LayoutInflater.from(context).inflate(R.layout.modify_stock_alert, null)
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Retirar stock")
            builder.setView(dialogView)
            builder.setPositiveButton("Ok") {dialog, which ->

                if (dialogView.alertModifyStock.text.toString() == "") {

                    dialogView.alertModifyStock.setText("0")

                }

                val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    fullDay = "$year-${monthOfYear+1}-$dayOfMonth"
                    ArticlesRepository(context).modificarStock(dialogView.alertModifyStock.text.toString().toInt()*-1, cursor.getInt(cursor.getColumnIndex("_id")), fullDay!!,"S")
                    val intent = Intent(context, MainActivity()::class.java)
                    context.startActivity(intent)
                }, year, month, day)

                dpd.show()

            }

            builder.setNegativeButton("Cancelar") {dialog, which ->}

            val dialog = builder.create()
            dialog.show()

        }



    }

}