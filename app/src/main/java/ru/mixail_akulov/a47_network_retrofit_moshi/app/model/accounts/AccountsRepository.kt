package ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts

import kotlinx.coroutines.flow.Flow
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.*
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.async.LazyFlowSubject
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.entities.Account
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.entities.SignUpData
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.settings.AppSettings

class AccountsRepository(
    private val accountsSource: AccountsSource,
    private val appSettings: AppSettings
) {

    private val accountLazyFlowSubject = LazyFlowSubject<Unit, Account> {
        doGetAccount()
    }

    /**
     * Вошел ли пользователь в систему или нет.
     */
    fun isSignedIn(): Boolean {
        // пользователь вошел в систему, если существует токен аутентификации
        return appSettings.getCurrentToken() != null
    }

    /**
     * Попробуйте войти с помощью электронной почты и пароля.
     * @throws EmptyFieldException
     * @throws InvalidCredentialsException
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun signIn(email: String, password: String) {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)

        val token = try {
            accountsSource.signIn(email, password)
        } catch (e: Exception) {
            if (e is BackendException && e.code == 401) {
                // сопоставить ошибку 401 для входа в InvalidCredentialsException
                throw InvalidCredentialsException(e)
            } else {
                throw e
            }
        }
        // успех! получил токен авторизации -> сохранить его
        appSettings.setCurrentToken(token)
        // и загрузить данные аккаунта
        accountLazyFlowSubject.updateAllValues(accountsSource.getAccount())
    }

    /**
     * Create a new account.
     * @throws EmptyFieldException
     * @throws PasswordMismatchException
     * @throws AccountAlreadyExistsException
     * @throws ConnectionException
     * @throws BackendException
     */
    suspend fun signUp(signUpData: SignUpData) {
        signUpData.validate()
        try {
            accountsSource.signUp(signUpData)
        } catch (e: BackendException) {
            // пользователь с таким адресом электронной почты уже существует
            if (e.code == 409) throw AccountAlreadyExistsException(e)
            else throw e
        }
    }


    /**
     * Обновить информацию об учетной записи.
     * Результаты перезагрузки доставляются в потоки, возвращаемые методом [getAccount].
     */
    fun reloadAccount() {
        accountLazyFlowSubject.reloadAll()
    }

    /**
     * Получите информацию об учетной записи текущего вошедшего пользователя и
     * прослушайте все дальнейшие изменения данных учетной записи.
     * Если пользователь не вошел в систему, выдается пустой результат.
     * @return бесконечный поток, всегда успех; ошибки переносятся в [Result]
     */
    fun getAccount(): Flow<Result<Account>> {
        return accountLazyFlowSubject.listen(Unit)
    }

    /**
     * Измените имя пользователя текущего вошедшего пользователя.
     * Обновленный объект учетной записи доставляется в поток,
     * возвращаемый вызовом [getAccount] после вызова этого метода.
     * @throws EmptyFieldException
     * @throws AuthException
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun updateAccountUsername(newUsername: String) = wrapBackendExceptions {
        if (newUsername.isBlank()) throw EmptyFieldException(Field.Username)
        accountsSource.setUsername(newUsername)
        accountLazyFlowSubject.updateAllValues(accountsSource.getAccount())
    }

    /**
     * Очистить JWT-токен, сохраненный в настройках.
     */
    fun logout() {
        appSettings.setCurrentToken(null)
        accountLazyFlowSubject.updateAllValues(null)
    }

    private suspend fun doGetAccount(): Account = wrapBackendExceptions {
        try {
            accountsSource.getAccount()
        } catch (e: BackendException) {
            // учетная запись была удалена = сеанс истек = AuthException
            if (e.code == 404) throw AuthException(e)
            else throw e
        }
    }

}