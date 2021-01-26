package com.nazmar.dicegainz

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.nazmar.dicegainz.database.LiftDatabase
import com.nazmar.dicegainz.repository.Repository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Repository.setDataSource(LiftDatabase.getInstance(application))

        AppCompatDelegate.setDefaultNightMode(
            getPreferences(Context.MODE_PRIVATE).getInt(
                getString(R.string.theme),
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        )
        setContentView(R.layout.activity_main)
    }
}