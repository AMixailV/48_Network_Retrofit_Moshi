package ru.mixail_akulov.a47_network_retrofit_moshi.app.model.settings

interface AppSettings {

    /**
     * Получить токен авторизации текущего вошедшего в систему пользователя.
     */
    fun getCurrentToken(): String?

    /**
     * Установите токен авторизации вошедшего в систему пользователя.
     */
    fun setCurrentToken(token: String?)

}
