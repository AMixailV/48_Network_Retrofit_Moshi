package ru.mixail_akulov.a47_network_retrofit_moshi.sources.accounts.entities

/**
 * Тело HTTP-запроса `PUT me` для обновления имени текущего вошедшего в систему пользователя.
 *
 * JSON example:
 * ```
 * {
 *   "username": "",
 * }
 * ```
 */
data class UpdateUsernameRequestEntity(
    val username: String
)