package ru.mixail_akulov.a47_network_retrofit_moshi.sources.accounts.entities

import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.entities.Account

/**
 * Response body for `GET /me` HTTP-request.
 *
 * JSON example:
 * ```
 * {
 *   "id": 0,
 *   "email": "",
 *   "username": "",
 *   "createdAt": 0
 * }
 * ```
 */
data class GetAccountResponseEntity(
    val id: Long,
    val email: String,
    val username: String,
    val createdAt: Long
) {

    /**
     * Преобразовать этот объект в экземпляр [Account] в приложении.
     */
    fun toAccount(): Account = Account(
        id = id,
        email = email,
        username = username,
        createdAt = createdAt
    )
}