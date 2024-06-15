package ru.mixail_akulov.a47_network_retrofit_moshi.sources.base

import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.SourcesProvider
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsSource
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.BoxesSource
import ru.mixail_akulov.a47_network_retrofit_moshi.sources.accounts.RetrofitAccountsSource
import ru.mixail_akulov.a47_network_retrofit_moshi.sources.boxes.RetrofitBoxesSource

/**
 * Создание исходников на базе Retrofit+Moshi.
 */
class RetrofitSourcesProvider(
    private val config: RetrofitConfig
) : SourcesProvider {

    override fun getAccountsSource(): AccountsSource {
        return RetrofitAccountsSource(config)
    }

    override fun getBoxesSource(): BoxesSource {
        return RetrofitBoxesSource(config)
    }

}