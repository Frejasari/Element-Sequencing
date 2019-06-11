package de.frejaundalex.elementsequencing

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import de.frejaundalex.elementsequencing.db.ObjectBox
import de.frejaundalex.elementsequencing.db.model.AsanaImageCategory
import de.frejaundalex.elementsequencing.db.model.Difficulty
import de.frejaundalex.elementsequencing.db.model.Element
import io.objectbox.android.AndroidObjectBrowser

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        ObjectBox.init(this)
        Element.all
        Difficulty.all
        AsanaImageCategory.all
        if (BuildConfig.DEBUG) {
            AndroidObjectBrowser(ObjectBox.get()).start(this)
        }
    }
}