package ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts

import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.BackendException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.ConnectionException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.ParseBackendResponseException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.entities.Account
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.entities.SignUpData

interface AccountsSource {

    /**
     * Выполнить запрос на вход.
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     * @return JWT-token
     */
    suspend fun signIn(email: String, password: String): String

    /**
     * Создать новый аккаунт.
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun signUp(signUpData: SignUpData)

    /**
     * Получить информацию об учетной записи текущего вошедшего пользователя.
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun getAccount(): Account

    /**
     * Изменить имя пользователя текущего вошедшего в систему пользователя.
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun setUsername(username: String)

}