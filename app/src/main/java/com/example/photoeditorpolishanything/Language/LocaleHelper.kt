//package com.example.photoeditorpolishanything.Language
//
////object LocaleHelper {
////    private const val SELECTED_LANGUAGE = "SelectedLanguage"
////
////    fun setLocale(context: Context, language: String) {
////        val locale = Locale(language)
////        Locale.setDefault(locale)
////
////        val resources = context.resources
////        val configuration = Configuration(resources.configuration)
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
////            configuration.setLocale(locale)
////        } else {
////            configuration.locale = locale
////        }
////
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////            context.createConfigurationContext(configuration)
////        } else {
////            resources.updateConfiguration(configuration, resources.displayMetrics)
////        }
////
////        // Save selected language to SharedPreferences
////        PreferenceManager.getDefaultSharedPreferences(context)
////            .edit()
////            .putString(SELECTED_LANGUAGE, language)
////            .apply()
////    }
////
////    fun getLocale(context: Context): Locale {
////        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
////        val language = preferences.getString(SELECTED_LANGUAGE, "en") ?: "en"
////        return Locale(language)
////    }
////}
//

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import java.util.Locale

object LocaleHelper {

    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): String {
        return getPersistedData(context, Locale.getDefault().language)
    }

    fun setLocale(context: Context, language: String): Context {
        persist(context, language)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else {
            updateResourcesLegacy(context, language)
        }
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage) ?: defaultLanguage
    }

    private fun persist(context: Context, language: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }
}
