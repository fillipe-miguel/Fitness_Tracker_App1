package br.com.example.fitnesstracker

import android.app.Application
import br.com.example.fitnesstracker.model.AppDatabase

class App : Application() {

	lateinit var db: AppDatabase

	override fun onCreate() {
		super.onCreate()
		db = AppDatabase.getDatabase(this)
	}
}