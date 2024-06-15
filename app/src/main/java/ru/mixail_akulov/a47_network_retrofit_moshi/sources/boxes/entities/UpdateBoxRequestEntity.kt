package ru.mixail_akulov.a47_network_retrofit_moshi.sources.boxes.entities

/**
 * Тело запроса `PUT box{id}` HTTP-запрос на активацию и деактивацию указанного ящика.
 *
 * JSON example:
 * ```
 * {
 *   "isActive": true
 * }
 * ```
 */
data class UpdateBoxRequestEntity(
    val isActive: Boolean
)