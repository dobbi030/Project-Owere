package com.blooburn.owere.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

//SharedPreferences 클래스는 앱에 있는 다른 액티비티보다 먼저 생성되어야 다른 곳에 데이터를 넘겨줄 수 있다.
// 이러기 위해서는 Application에 해당하는 클래스를 생성한 뒤, 전역 변수로 SharedPreferences를 가지고 있어야 한다.

//MyApplication 클래스를 생성했다면, Manifest에 등록해줘야 한다.
//


class ApplicationForShared : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}

//getString()과 setString() 메소드를 만들어서 SharedPreferences에 접근하여 데이터를 저장하거나 가져올 것


class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getShared(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setShared(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
}



