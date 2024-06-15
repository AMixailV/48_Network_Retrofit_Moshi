package ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.mixail_akulov.a47_network_retrofit_moshi.app.R
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.MutableLiveEvent
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.MutableUnitLiveEvent
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.publishEvent
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.share
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.AuthException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.BackendException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.ConnectionException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.Logger

open class BaseViewModel(
    val accountsRepository: AccountsRepository,
    val logger: Logger
) : ViewModel() {

    private val _showErrorMessageResEvent = MutableLiveEvent<Int>()
    val showErrorMessageResEvent = _showErrorMessageResEvent.share()

    private val _showErrorMessageEvent = MutableLiveEvent<String>()
    val showErrorMessageEvent = _showErrorMessageEvent.share()

    private val _showAuthErrorAndRestartEvent = MutableUnitLiveEvent()
    val showAuthErrorAndRestartEvent = _showAuthErrorAndRestartEvent.share()

    fun CoroutineScope.safeLaunch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                block.invoke(this)
            } catch (e: ConnectionException) {
                logError(e)
                _showErrorMessageResEvent.publishEvent(R.string.connection_error)
            } catch (e: BackendException) {
                logError(e)
                _showErrorMessageEvent.publishEvent(e.message ?: "")
            } catch (e: AuthException) {
                logError(e)
                _showAuthErrorAndRestartEvent.publishEvent()
            } catch (e: Exception) {
                logError(e)
                _showErrorMessageResEvent.publishEvent(R.string.internal_error)
            }
        }
    }

    fun logError(e: Throwable) {
        logger.error(javaClass.simpleName, e)
    }

    fun logout() {
        accountsRepository.logout()
    }

}