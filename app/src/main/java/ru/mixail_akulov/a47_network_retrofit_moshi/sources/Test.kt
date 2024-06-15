package ru.mixail_akulov.a47_network_retrofit_moshi.sources

import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class CategoriesResponseBody(
    val id: String,
    val name: String
)

interface CategoriesApi {
    @GET("getCategories.php")
    suspend fun getCategories(): List<CategoriesResponseBody>
}

fun main() = runBlocking {
    val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    val moshi = Moshi.Builder().build()

    val moshiConverterFactory = MoshiConverterFactory.create(moshi)

    val retrofit = Retrofit.Builder()
        .baseUrl("http://shop")
        .client(client)
        .addConverterFactory(moshiConverterFactory)
        .build()

    val api = retrofit.create(CategoriesApi::class.java)

    val response = api.getCategories()

    response.forEach { println("${it.id} ${it.name}") }
}
