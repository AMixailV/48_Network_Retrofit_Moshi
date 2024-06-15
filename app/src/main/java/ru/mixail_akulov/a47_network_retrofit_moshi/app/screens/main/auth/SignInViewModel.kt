package ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.main.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ru.mixail_akulov.a47_network_retrofit_moshi.app.R
import ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.base.BaseViewModel
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.*
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.LogCatLogger
import ru.mixail_akulov.a47_network_retrofit_moshi.app.Singletons
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.EmptyFieldException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.Field
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.InvalidCredentialsException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.logger.Logger

class SignInViewModel(
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

    private val _state = MutableLiveData(State())
    val state = _state.share()

    private val _clearPasswordEvent = MutableUnitLiveEvent()
    val clearPasswordEvent = _clearPasswordEvent.share()

    private val _showAuthErrorToastEvent = MutableLiveEvent<Int>()
    val showAuthToastEvent = _showAuthErrorToastEvent.share()

    private val _navigateToTabsEvent = MutableUnitLiveEvent()
    val navigateToTabsEvent = _navigateToTabsEvent.share()

    fun signIn(email: String, password: String) = viewModelScope.safeLaunch {
        showProgress()
        try {
            accountsRepository.signIn(email, password)
            launchTabsScreen()
        } catch (e: EmptyFieldException) {
            processEmptyFieldException(e)
        } catch (e: InvalidCredentialsException) {
            processInvalidCredentialsException()
        } finally {
            hideProgress()
        }
    }

    private fun processEmptyFieldException(e: EmptyFieldException) {
        _state.value = _state.requireValue().copy(
            emptyEmailError = e.field == Field.Email,
            emptyPasswordError = e.field == Field.Password
        )
    }

    private fun processInvalidCredentialsException() {
        clearPasswordField()
        showAuthErrorToast()
    }

    private fun showProgress() {
        _state.value = State(signInInProgress = true)
    }

    private fun hideProgress() {
        _state.value = _state.requireValue().copy(signInInProgress = false)
    }

    private fun clearPasswordField() = _clearPasswordEvent.publishEvent()

    private fun showAuthErrorToast() = _showAuthErrorToastEvent.publishEvent(R.string.invalid_email_or_password)

    private fun launchTabsScreen() = _navigateToTabsEvent.publishEvent()

    data class State(
        val emptyEmailError: Boolean = false,
        val emptyPasswordError: Boolean = false,
        val signInInProgress: Boolean = false
    ) {
        val showProgress: Boolean get() = signInInProgress
        val enableViews: Boolean get() = !signInInProgress
    }
}