package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.playlistmaker.di.appModule
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.Locale

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, domainModule, dataModule))
        }
        val languageTag = "ru"
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.create(Locale.forLanguageTag(languageTag))
        )
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
    }
}