package ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.async

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.Result

typealias SuspendValueLoader<A, T> = suspend (A) -> T?

/**
 * То же, что и [LazyListenersSubject], но адаптировано для использования с потоками kotlin.
 * @see LazyListenersSubject
 */
class LazyFlowSubject<A : Any, T : Any>(
    private val loader: SuspendValueLoader<A, T>
) {

    private val lazyListenersSubject = LazyListenersSubject<A, T> {
        runBlocking {
            loader.invoke(it)
        }
    }

    /**
     * @see [LazyListenersSubject.reloadAll]
     */
    fun reloadAll(silentMode: Boolean = false) {
        lazyListenersSubject.reloadAll(silentMode)
    }

    /**
     * @see LazyListenersSubject.reloadArgument
     */
    fun reloadArgument(argument: A, silentMode: Boolean = false) {
        lazyListenersSubject.reloadArgument(argument, silentMode)
    }

    /**
     * @see LazyListenersSubject.updateAllValues
     */
    fun updateAllValues(newValue: T?) {
        lazyListenersSubject.updateAllValues(newValue)
    }

    /**
     * @see LazyListenersSubject.addListener
     * @see LazyListenersSubject.removeListener
     */
    fun listen(argument: A): Flow<Result<T>> = callbackFlow {
        val listener: ValueListener<T> = { result ->
            trySend(result)
        }
        lazyListenersSubject.addListener(argument, listener)
        awaitClose {
            lazyListenersSubject.removeListener(argument, listener)
        }
    }

}