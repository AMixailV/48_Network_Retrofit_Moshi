package ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.main.tabs.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.base.BaseViewModel
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.share
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.LogCatLogger
import ru.mixail_akulov.a47_network_retrofit_moshi.app.Singletons
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.Result
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.BoxesRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.Box
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.BoxAndSettings
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.BoxesFilter
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.Logger

class SettingsViewModel(
    private val boxesRepository: BoxesRepository = Singletons.boxesRepository,
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger), SettingsAdapter.Listener {

    private val _boxSettings = MutableLiveData<Result<List<BoxAndSettings>>>()
    val boxSettings = _boxSettings.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ALL).collect {
                _boxSettings.value = it
            }
        }
    }

    fun tryAgain() = viewModelScope.safeLaunch {
        boxesRepository.reload(BoxesFilter.ALL)
    }

    override fun enableBox(box: Box) = viewModelScope.safeLaunch {
        boxesRepository.activateBox(box)
    }

    override fun disableBox(box: Box) = viewModelScope.safeLaunch {
        boxesRepository.deactivateBox(box)
    }

}