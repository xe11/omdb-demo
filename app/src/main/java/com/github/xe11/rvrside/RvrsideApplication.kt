package com.github.xe11.rvrside

import android.app.Application
import com.github.xe11.rvrside.core.di.ApplicationComponent
import com.github.xe11.rvrside.core.di.DaggerApplicationComponent

internal class RvrsideApplication : Application() {

    private val appComponent by lazy(::createAppComponent)

    private fun createAppComponent(): ApplicationComponent =
        DaggerApplicationComponent.create()


    override fun getSystemService(name: String): Any {
        return when (name) {
            ApplicationComponent::class.java.name -> appComponent
            else -> super.getSystemService(name)
        }
    }
}
