package com.androdevelopment.patient.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(private val context: Context) {

    private val PREFS_NAME = "my_prefs"

    fun putInt(key: String?, value: Int) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String?, defValue: Int): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getInt(key, defValue)
    }

    fun putString(key: String?, value: String?) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String?): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getString(key, "null")
    }

    fun putBool(key: String?, value: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBool(key: String?): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getBoolean(key, false)
    }

    fun getBool(key: String?, defaultValue: Boolean): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getBoolean(key, defaultValue)
    }

}