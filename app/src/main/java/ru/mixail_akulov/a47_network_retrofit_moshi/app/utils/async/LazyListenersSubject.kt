package ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.async

import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


typealias ValueLoader<A, T> = (A) -> T? // загрузчик значений
typealias ValueListener<T> = (Result<T>) -> Unit  // прослушиватель значений

/**
 * Когда через вызов [addListener] добавляется хотя бы один слушатель, выполняется [loader],
 * и статус его выполнения передается слушателям.
 * Добавление других прослушивателей с тем же аргументом не запускает новую загрузку,
 * но повторно используется существующая.
 * Кроме того, загрузка отменяется и ресурсы освобождаются,
 * когда последний прослушиватель для указанного аргумента удаляется путем вызова [removeListener].
 */
class LazyListenersSubject<A : Any, T : Any>(
    // для реального сервера лучше использовать пул кэшированных потоков.
    private val loaderExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    // однопоточный пул, чтобы избежать проблем с многопоточностью
    private val handlerExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    private val loader: ValueLoader<A, T>
) {

    private val listeners = mutableListOf<ListenerRecord<A, T>>()
    private val futures = mutableMapOf<A, FutureRecord<T>>()

    /**
     * Добавьте новый слушатель, который получает значение,
     * созданное [loader] с указанным аргументом.
     * Значение создается и кэшируется [loader],
     * когда добавляется первый прослушиватель с указанным аргументом.
     * Все последующие прослушиватели будут повторно использовать
     * одно и то же кэшированное значение без запуска [loader].
     */
    fun addListener(argument: A, listener: ValueListener<T>) = handlerExecutor.execute {
        val listenerRecord = ListenerRecord(argument, listener)
        listeners.add(listenerRecord)
        val futureRecord = futures[argument]
        if (futureRecord == null) {
            execute(argument)
        } else {
            listener.invoke(futureRecord.lastValue)
        }
    }

    /**
     * Удалите прослушиватель.
     * Если последний слушатель удален для указанного аргумента,
     * загрузчик значений для этого аргумента отменяется.
     */
    fun removeListener(argument: A, listener: ValueListener<T>) = handlerExecutor.execute {
        listeners.removeAll { it.listener == listener && it.arg == argument }
        if (!listeners.any { it.arg == argument }) {
            cancel(argument)
        }
    }

    /**
     * Перезагрузить все кэшированные данные.
     * [silentMode] — если установлено значение «true», результат [Pending] не передается слушателям
     */
    fun reloadAll(silentMode: Boolean = false) = handlerExecutor.execute {
        futures.forEach { entry ->
            val argument = entry.key
            val record = entry.value
            restart(argument, record, silentMode)
        }
    }

    /**
     * Перезагрузить кэшированные данные для указанного аргумента.
     */
    fun reloadArgument(argument: A, silentMode: Boolean = false) = handlerExecutor.execute {
        val record = futures[argument] ?: return@execute
        restart(argument, record, silentMode)
    }

    /**
     * Вручную обновите все значения, не запуская [loader].
     */
    fun updateAllValues(newValue: T?) {
        futures.forEach {
            val result = if (newValue == null) Empty() else Success(newValue)
            it.value.lastValue = result
            publish(it.key, result)
        }
    }

    private fun cancel(argument: A) {
        val record = futures[argument]
        if (record != null) {
            futures.remove(argument)
            record.future?.cancel(true)
        }
    }

    private fun execute(argument: A, silentMode: Boolean = false) {
        val record = FutureRecord<T>(null, Pending())
        futures[argument] = record
        val future = loaderExecutor.submit {
            try {
                if (!silentMode) publishIfNotCancelled(record, argument, Pending())
                val res = loader(argument)
                if (res == null) {
                    publishIfNotCancelled(record, argument, Empty())
                } else {
                    publishIfNotCancelled(record, argument, Success(res))
                }
            } catch (e: Exception) {
                publishIfNotCancelled(record, argument, Error(e))
            }
        }
        record.future = future
    }

    private fun publishIfNotCancelled(record: FutureRecord<T>, argument: A, result: Result<T>) {
        if (record.cancelled) return
        publish(argument, result)
    }

    private fun publish(argument: A, result: Result<T>) = handlerExecutor.execute {
        futures.filter { it.key == argument }.forEach {
            it.value.lastValue = result
        }
        listeners.filter { it.arg == argument }.forEach {
            it.listener.invoke(result)
        }
    }

    private fun restart(argument: A, record: FutureRecord<T>, silentMode: Boolean) {
        record.cancelled = true
        record.future?.cancel(true)
        execute(argument, silentMode)
    }

    private class ListenerRecord<A, T>(
        val arg: A,
        val listener: ValueListener<T>
    )

    private class FutureRecord<T>(
        var future: Future<*>?,
        var lastValue: Result<T> = Empty(),
        var cancelled: Boolean = false
    )
}
