package com.example.articlessqlite

public class Article {

    private var id: Int = 0
    private var code: String = ""
    private var descripcio: String = ""
    private var pvp: Double = 0.0
    private var stock: Int = 0

    fun getId(): Int {
        return id
    }
    fun setId(id: Int) {
        this.id = id
    }
    fun getCode(): String {
        return code
    }
    fun setCode(code: String) {
        this.code = code
    }
    fun getDescripcio(): String {
        return descripcio
    }
    fun setDescripcio(descripcio: String) {
        this.descripcio = descripcio
    }
    fun getPVP(): Double {
        return pvp
    }
    fun setPVP(pvp: Double) {
        this.pvp = pvp
    }
    fun getStock(): Int {
        return stock
    }
    fun setStock(stock: Int) {
        this.stock = stock
    }





}