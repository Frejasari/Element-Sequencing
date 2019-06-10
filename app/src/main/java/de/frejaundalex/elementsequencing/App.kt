package de.frejaundalex.elementsequencing

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import de.frejaundalex.elementsequencing.db.ObjectBox
import de.frejaundalex.elementsequencing.db.model.Element

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        ObjectBox.init(this)
        Element.all
    }
}