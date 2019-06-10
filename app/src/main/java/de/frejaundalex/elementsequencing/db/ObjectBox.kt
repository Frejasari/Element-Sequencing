package de.frejaundalex.elementsequencing.db

import android.content.Context
import de.frejaundalex.elementsequencing.db.model.MyObjectBox
import io.objectbox.BoxStore


object ObjectBox {

    private lateinit var boxStore: BoxStore

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }

    fun get(): BoxStore {
        return boxStore
    }
}