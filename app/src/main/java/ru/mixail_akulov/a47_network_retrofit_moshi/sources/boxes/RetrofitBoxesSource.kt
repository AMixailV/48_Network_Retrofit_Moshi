package ru.mixail_akulov.a47_network_retrofit_moshi.sources.boxes

import kotlinx.coroutines.delay
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.BoxesSource
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.BoxAndSettings
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.BoxesFilter
import ru.mixail_akulov.a47_network_retrofit_moshi.sources.base.BaseRetrofitSource
import ru.mixail_akulov.a47_network_retrofit_moshi.sources.base.RetrofitConfig
import ru.mixail_akulov.a47_network_retrofit_moshi.sources.boxes.entities.UpdateBoxRequestEntity

class RetrofitBoxesSource(
    config: RetrofitConfig
) : BaseRetrofitSource(config), BoxesSource {

    private val boxesApi = retrofit.create(BoxesApi::class.java)

    override suspend fun getBoxes(
        boxesFilter: BoxesFilter
    ): List<BoxAndSettings> = wrapRetrofitExceptions {
        delay(500)
        val isActive: Boolean? = if (boxesFilter == BoxesFilter.ONLY_ACTIVE)
            true
        else
            null

        boxesApi.getBoxes(isActive).map { it.toBoxAndSettings() }
    }

    override suspend fun setIsActive(
        boxId: Long,
        isActive: Boolean
    ) = wrapRetrofitExceptions {
        val updateBoxRequestEntity = UpdateBoxRequestEntity(isActive)
        boxesApi.setIsActive(boxId, updateBoxRequestEntity)
    }

}