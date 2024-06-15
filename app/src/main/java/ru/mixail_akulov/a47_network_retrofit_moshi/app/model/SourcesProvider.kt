package ru.mixail_akulov.a47_network_retrofit_moshi.app.model

import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsSource
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.BoxesSource

/**
 * Фабричный класс для всех сетевых источников.
 */
interface SourcesProvider {

    /**
     * Создайте [AccountsSource], который отвечает за чтение и запись данных учетных записей пользователей.
     */
    fun getAccountsSource(): AccountsSource

    /**
     * Создайте [BoxesSource], который отвечает за чтение данных об обновлении ящиков.
     */
    fun getBoxesSource(): BoxesSource

}