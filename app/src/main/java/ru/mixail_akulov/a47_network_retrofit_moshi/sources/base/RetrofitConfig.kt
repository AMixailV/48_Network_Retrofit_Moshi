package ru.mixail_akulov.a47_network_retrofit_moshi.sources.base

import com.squareup.moshi.Moshi
import retrofit2.Retrofit

/**
 * Все необходимое для выполнения HTTP-запросов с помощью клиента Retrofit и для парсинга JSON-сообщений.
 */
class RetrofitConfig(
    val retrofit: Retrofit,
    val moshi: Moshi
)