package ru.mixail_akulov.a47_network_retrofit_moshi.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.splash.SplashFragment
import ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.splash.SplashViewModel

/**
 * Точка входа в приложение.
 *
 * Splash-активность содержит только фон окна, вся остальная логика инициализации размещается в
 * [SplashFragment] и [SplashViewModel].
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Singletons.init(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

}
