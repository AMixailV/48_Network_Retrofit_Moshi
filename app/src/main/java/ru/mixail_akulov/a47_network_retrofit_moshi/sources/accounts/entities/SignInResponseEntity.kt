package ru.mixail_akulov.a47_network_retrofit_moshi.sources.accounts.entities

/**
 * Тело ответа на HTTP-запрос `POST вход`. Содержит JWT-токен,
 * который следует размещать в других запросах как значение заголовка «Авторизация».
 *
 * JSON example:
 * ```
 * {
 *   "token": "jwt-token",
 * }
 * ```
 */
data class SignInResponseEntity(
    val token: String
)