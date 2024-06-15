package ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.*
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.AccountsRepository
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.async.LazyFlowSubject
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.accounts.entities.Account
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.Box
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.BoxAndSettings
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.BoxesFilter

class BoxesRepository(
    private val accountsRepository: AccountsRepository,
    private val boxesSource: BoxesSource
) {

    private var accountResult: Result<Account> = Empty()

    private val boxesLazyFlowSubject = LazyFlowSubject<BoxesFilter, List<BoxAndSettings>> { filter ->
        wrapBackendExceptions { boxesSource.getBoxes(filter) }
    }

    /**
     * Получите список ящиков.
     * @return бесконечный поток, всегда успех; ошибки переносятся в [Result]
     */
    fun getBoxesAndSettings(filter: BoxesFilter): Flow<Result<List<BoxAndSettings>>> {
        return accountsRepository.getAccount()
            .onEach {
                accountResult = it
            }
            .flatMapLatest { result ->
                if (result is Success) {
                    //есть новые данные аккаунта -> перезагрузить ящики
                    boxesLazyFlowSubject.listen(filter)
                } else {
                    flowOf(result.map())
                }
            }
    }

    /**
     * Reload the list of boxes.
     * @throws AuthException
     * @throws BackendException
     * @throws ConnectionException
     */
    fun reload(filter: BoxesFilter) {
        if (accountResult is Error) {
            // не удалось загрузить учетную запись -> попробуйте еще раз;
            // после загрузки аккаунта ящики будут загружены автоматически
            accountsRepository.reloadAccount()
        } else {
            boxesLazyFlowSubject.reloadArgument(filter)
        }
    }

    /**
     * Отметьте указанный флажок как активный. На экране информационной панели отображаются только активные поля.
     * @throws AuthException
     * @throws BackendException
     * @throws ConnectionException
     */
    suspend fun activateBox(box: Box) = wrapBackendExceptions {
        boxesSource.setIsActive(box.id, true)
        boxesLazyFlowSubject.reloadAll(silentMode = true)
    }

    /**
     * Отметьте указанный флажок как неактивный. Неактивные поля не отображаются на экране панели управления.
     * @throws AuthException
     * @throws BackendException
     * @throws ConnectionException
     */
    suspend fun deactivateBox(box: Box) = wrapBackendExceptions {
        boxesSource.setIsActive(box.id, false)
        boxesLazyFlowSubject.reloadAll(silentMode = true)
    }

}