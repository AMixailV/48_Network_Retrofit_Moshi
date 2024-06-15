package ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.MutableLiveEvent
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.publishEvent
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.share
import ru.mixail_akulov.a47_network_retrofit_moshi.app.Singletons

/**
 * SplashViewModel проверяет, вошел ли пользователь в систему или нет.
 */
class SplashViewModel(
    private val accountsRepository: AccountsRepository = Singletons.accountsRepository
) : ViewModel() {

    private val _launchMainScreenEvent = MutableLiveEvent<Boolean>()
    val launchMainScreenEvent = _launchMainScreenEvent.share()

    init {
        viewModelScope.launch {
            _launchMainScreenEvent.publishEvent(accountsRepository.isSignedIn())
        }
    }
}