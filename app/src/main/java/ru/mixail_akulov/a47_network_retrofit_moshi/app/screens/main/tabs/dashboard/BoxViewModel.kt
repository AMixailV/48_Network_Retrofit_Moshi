package ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.main.tabs.dashboard

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.base.BaseViewModel
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.MutableLiveEvent
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.publishEvent
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.share
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.LogCatLogger
import ru.mixail_akulov.a47_network_retrofit_moshi.app.Singletons
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.Success
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.BoxesRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.BoxesFilter
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.Logger

class BoxViewModel(
    private val boxId: Long,
    private val boxesRepository: BoxesRepository = Singletons.boxesRepository,
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

    private val _shouldExitEvent = MutableLiveEvent<Boolean>()
    val shouldExitEvent = _shouldExitEvent.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ONLY_ACTIVE)
                .map { res -> res.map { boxes -> boxes.firstOrNull { it.box.id == boxId } } }
                .collect { res ->
                    _shouldExitEvent.publishEvent(res is Success && res.value == null)
                }
        }
    }
}