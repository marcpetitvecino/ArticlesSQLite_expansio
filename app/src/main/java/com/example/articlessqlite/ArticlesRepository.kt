package com.example.articlessqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf


val DATABASE_NAME = "Bdd"
val DATABASE_VERSION = 2
// Taula principal
val TABLE_NAME = "ARTICLES"
val COL_ID = "_id"
val COL_CODE = "Codi"
val COL_DESCRIPCIO = "Descripcio"
val COL_PVP = "PVP"
val COL_STOCK = "STOCK"

// Taula expansió
val TABLE_2_NAME = "MOVIMENT"
val COL_ID_2 = "_id"
val COL_CODI = "CODIARTICLE"
val COL_DIA = "DIA"
val COL_QUANTITAT = "QUANTITAT"
val COL_TIPUS = "TIPUS"

class ArticlesRepository(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {



        val createTableA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("+
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_CODE + " VARCHAR(80)," +
                COL_DESCRIPCIO + " VARCHAR(80)," +
                COL_STOCK + " INT DEFAULT 0," +
                COL_PVP + " DOUBLE)";

        val createTableB = "CREATE TABLE IF NOT EXISTS " + TABLE_2_NAME + " ("+
                COL_ID_2 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CODI + " TEXT," +
                COL_DIA + " TEXT," +
                COL_QUANTITAT + " INT," +
                COL_TIPUS + " VARCHAR(1)," +
                " FOREIGN KEY ($COL_CODI) REFERENCES $TABLE_NAME($COL_CODE))";

        p0?.execSQL(createTableA)
        p0?.execSQL(createTableB)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        if (p1 < DATABASE_VERSION) {

            val createTableB = "CREATE TABLE IF NOT EXISTS " + TABLE_2_NAME + " ("+
                    COL_ID_2 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_CODI + " TEXT," +
                    COL_DIA + " TEXT," +
                    COL_QUANTITAT + " INT," +
                    COL_TIPUS + " VARCHAR(1)," +
                    " FOREIGN KEY ($COL_CODI) REFERENCES $TABLE_NAME($COL_CODE))";
            p0?.execSQL(createTableB)

        }

    }


    fun cursorToArticle(cursor: Cursor): Article? {

        val newArticle = Article()

        if (cursor.count > 0) {

            newArticle.setCode(cursor.getString(cursor.getColumnIndex(COL_CODE)))
            newArticle.setDescripcio(cursor.getString(cursor.getColumnIndex(COL_DESCRIPCIO)))
            newArticle.setStock(cursor.getInt(cursor.getColumnIndex(COL_STOCK)))
            newArticle.setPVP(cursor.getDouble(cursor.getColumnIndex(COL_PVP)))

            return newArticle

        }

        return null

    }

    fun getDataCursor(): Cursor {

        var db = this.readableDatabase
        var query = "SELECT $COL_ID, $COL_CODE, $COL_DESCRIPCIO, $COL_PVP, $COL_STOCK FROM $TABLE_NAME"

        var result = db.rawQuery(query, null)

        return result

    }

    fun getMovimentsCursor(dataInici: String = "01/01/0001", dataFi: String = "31/12/9999", codi: String): Cursor {

        var db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_2_NAME WHERE $COL_CODI='$codi' AND $COL_DIA BETWEEN '$dataInici' AND '$dataFi'"
        val result = db.rawQuery(query, null)
        return result

    }

    fun insertarArticle(codi: String, descripcio: String, pvp: Double, stock: Int) {

        var db = this.writableDatabase

        val values = ContentValues()
        values.put(COL_CODE, codi)
        values.put(COL_DESCRIPCIO, descripcio)
        values.put(COL_STOCK, stock)
        values.put(COL_PVP, pvp)

        db.insert(TABLE_NAME, null, values)
        db.close()

    }

    fun checkIfCodeExists(codi:String): Boolean {

        var db = this.readableDatabase
        var query = "SELECT $COL_CODE FROM $TABLE_NAME WHERE $COL_CODE = '$codi'"

        var cursor = db.rawQuery(query, null)

        return !cursor.moveToFirst()

    }

    fun findArticleById(id: Int): Article {

        var db = this.readableDatabase
        var query = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = '$id'"

        var cursor = db.rawQuery(query, null)

        cursor.moveToFirst()

        val retorn = cursorToArticle(cursor)!!

        cursor.close()

        return retorn

    }

    fun updateArticle(id: Int, descripcio: String, stock: Int, pvp: Double) {

        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_DESCRIPCIO, descripcio)
        cv.put(COL_PVP, pvp)
        cv.put(COL_STOCK, stock)

        db.update(TABLE_NAME, cv,"_id=$id", null)
        db.close()

    }

    fun esborrarArticle(id: Int) {

        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME WHERE _id=$id"

        db.execSQL(query)
        db.close()

    }

    fun searchByDescription(description: String): Cursor {

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_DESCRIPCIO LIKE '%$description%'"

        val result = db.rawQuery(query, null)

        return result

    }

    fun getCodiById(id: Int): String {

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = $id"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val article = cursorToArticle(cursor)

        return article!!.getCode()

    }

    fun getStockById(id: Int): Int {

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = $id"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val article = cursorToArticle(cursor)

        return article!!.getStock()

    }

    fun modificarStock(stock: Int, id: Int, date: String, tipus: String) {

        val db = this.writableDatabase
        val query = "INSERT INTO $TABLE_2_NAME($COL_CODI, $COL_DIA, $COL_QUANTITAT, $COL_TIPUS) VALUES('${getCodiById(id)}', '$date', $stock, '$tipus')"
        val query2 = "UPDATE $TABLE_NAME SET $COL_STOCK=${getStockById(id)+stock} WHERE $COL_ID=$id"

        db.execSQL(query)
        db.execSQL(query2)
        db.close()

    }

}