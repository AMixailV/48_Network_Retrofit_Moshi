package ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes

import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.BackendException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.ConnectionException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.ParseBackendResponseException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.BoxAndSettings
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.BoxesFilter

interface BoxesSource {

    /**
     * Получите список всех ящиков для текущего вошедшего в систему пользователя.
     * @throws BackendException
     * @throws ConnectionException
     * @throws ParseBackendResponseException
     */
    suspend fun getBoxes(boxesFilter: BoxesFilter): List<BoxAndSettings>

    /**
     * Установите флаг isActive для указанного поля.
     * @throws BackendException
     * @throws ConnectionException
     * @throws ParseBackendResponseException
     */
    suspend fun setIsActive(boxId: Long, isActive: Boolean)

}
