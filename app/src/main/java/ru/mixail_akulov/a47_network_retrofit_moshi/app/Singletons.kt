package ru.mixail_akulov.a47_network_retrofit_moshi.app

import android.content.Context
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.settings.SharedPreferencesAppSettings
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.SourcesProvider
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsSource
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.BoxesRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.BoxesSource
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.settings.AppSettings
import ru.mixail_akulov.a47_network_retrofit_moshi.sources.SourceProviderHolder

object Singletons {

    private lateinit var appContext: Context

    private val sourcesProvider: SourcesProvider by lazy {
        SourceProviderHolder.sourcesProvider
    }

    val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(appContext)
    }

    // --- sources

    private val accountsSource: AccountsSource by lazy {
        sourcesProvider.getAccountsSource()
    }

    private val boxesSource: BoxesSource by lazy {
        sourcesProvider.getBoxesSource()
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        AccountsRepository(
            accountsSource = accountsSource,
            appSettings = appSettings
        )
    }

    val boxesRepository: BoxesRepository by lazy {
        BoxesRepository(
            accountsRepository = accountsRepository,
            boxesSource = boxesSource
        )
    }

    /**
     * Вызовите этот метод во всех компонентах приложения, которые могут быть созданы
     * при восстановлении запуска приложения (например, в onCreate действий и служб).
     */
    fun init(appContext: Context) {
        Singletons.appContext = appContext
    }
}

