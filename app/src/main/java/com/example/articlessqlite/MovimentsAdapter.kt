package com.example.articlessqlite

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

class MovimentsAdapter(val context: Context, cursor: Cursor): CursorAdapter(context, cursor, true) {

    private lateinit var movimentCodi: TextView
    private lateinit var movimentDia: TextView
    private lateinit var movimentStock: TextView
    private lateinit var movimentTipus: TextView

    override fun newView(p0: Context?, p1: Cursor?, p2: ViewGroup?): View {

        return LayoutInflater.from(context).inflate(R.layout.moviments_cell, p2, false)

    }

    override fun bindView(p0: View?, p1: Context?, p2: Cursor?) {
        movimentCodi = p0!!.findViewById(R.id.movimentsCodi)
        movimentDia = p0.findViewById(R.id.movimentsDia)
        movimentStock = p0.findViewById(R.id.movimentsStock)
        movimentTipus = p0.findViewById(R.id.movimentsTipus)

        val codi = cursor.getString(cursor.getColumnIndex(COL_CODI))
        val dia = cursor.getString(cursor.getColumnIndex(COL_DIA))
        val stock = cursor.getInt(cursor.getColumnIndex(COL_QUANTITAT))
        val tipus = cursor.getString(cursor.getColumnIndex(COL_TIPUS))

        movimentCodi.text = "Codi: $codi"
        movimentDia.text = "Dia: $dia"
        movimentStock.text = "Stock: $stock"
        movimentTipus.text = "Tipus: $tipus"

    }


}