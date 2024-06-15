package ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.main.tabs.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.mixail_akulov.a47_network_retrofit_moshi.app.R
import ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.base.BaseViewModel
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.MutableLiveEvent
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.MutableUnitLiveEvent
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.publishEvent
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.share
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.LogCatLogger
import ru.mixail_akulov.a47_network_retrofit_moshi.app.Singletons
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.EmptyFieldException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.Success
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.Logger

class EditProfileViewModel(
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

    private val _initialUsernameEvent = MutableLiveEvent<String>()
    val initialUsernameEvent = _initialUsernameEvent.share()

    private val _saveInProgress = MutableLiveData(false)
    val saveInProgress = _saveInProgress.share()

    private val _goBackEvent = MutableUnitLiveEvent()
    val goBackEvent = _goBackEvent.share()

    private val _showErrorEvent = MutableLiveEvent<Int>()
    val showErrorEvent = _showErrorEvent.share()

    init {
        viewModelScope.launch {
            val res = accountsRepository.getAccount()
                .filter { it.isFinished() }
                .first()
            if (res is Success) _initialUsernameEvent.publishEvent(res.value.username)
        }
    }

    fun saveUsername(newUsername: String) = viewModelScope.safeLaunch {
        showProgress()
        try {
            accountsRepository.updateAccountUsername(newUsername)
            goBack()
        } catch (e: EmptyFieldException) {
            showEmptyFieldErrorMessage()
        } finally {
            hideProgress()
        }
    }

    private fun goBack() = _goBackEvent.publishEvent()

    private fun showProgress() {
        _saveInProgress.value = true
    }

    private fun hideProgress() {
        _saveInProgress.value = false
    }

    private fun showEmptyFieldErrorMessage() = _showErrorEvent.publishEvent(R.string.field_is_empty)

}