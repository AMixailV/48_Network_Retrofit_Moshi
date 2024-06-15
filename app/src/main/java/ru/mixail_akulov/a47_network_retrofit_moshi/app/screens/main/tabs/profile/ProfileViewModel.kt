package ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.main.tabs.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.base.BaseViewModel
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.share
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.LogCatLogger
import ru.mixail_akulov.a47_network_retrofit_moshi.app.Singletons
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.Result
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.entities.Account
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.Logger

class ProfileViewModel(
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

    private val _account = MutableLiveData<Result<Account>>()
    val account = _account.share()

    init {
        viewModelScope.launch {
            accountsRepository.getAccount().collect {
                _account.value = it
            }
        }
    }

    fun reload() {
        accountsRepository.reloadAccount()
    }

}