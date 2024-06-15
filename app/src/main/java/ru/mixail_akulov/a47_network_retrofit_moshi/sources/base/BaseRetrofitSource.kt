package ru.mixail_akulov.a47_network_retrofit_moshi.sources.base

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Retrofit
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.AppException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.BackendException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.ConnectionException
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.ParseBackendResponseException
import java.io.IOException

/**
 * Базовый класс для всех источников OkHttp.
 */
open class BaseRetrofitSource(
    retrofitConfig: RetrofitConfig
) {

    val retrofit: Retrofit = retrofitConfig.retrofit

    private val moshi: Moshi = retrofitConfig.moshi
    private val errorAdapter = moshi.adapter(ErrorResponseBody::class.java)

    /**
     * Сопоставьте сетевые и синтаксические исключения с исключениями внутри приложения.
     * @throws BackendException
     * @throws ParseBackendResponseException
     * @throws ConnectionException
     */
    suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: AppException) {
            throw e
        // moshi
        } catch (e: JsonDataException) {
            throw ParseBackendResponseException(e)
        } catch (e: JsonEncodingException) {
            throw ParseBackendResponseException(e)
        // retrofit
        } catch (e: HttpException) {
            throw createBackendException(e)
        // mostly retrofit
        } catch (e: IOException) {
            throw ConnectionException(e)
        }
    }

    private fun createBackendException(e: HttpException): Exception {
        return try {
            val errorBody: ErrorResponseBody = errorAdapter.fromJson(
                e.response()!!.errorBody()!!.string()
            )!!
            BackendException(e.code(), errorBody.error)
        } catch (e: Exception) {
            throw ParseBackendResponseException(e)
        }
    }

    class ErrorResponseBody(
        val error: String
    )

}