package ru.mixail_akulov.a47_network_retrofit_moshi.sources

import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

data class AccountResponseBody(
    val id: String,
    val email: String,
    val password: String,
    val username: String,
    val createdAt: String
)

interface Api {
    @GET("getAccounts.php")
    suspend fun getAccounts(): List<AccountResponseBody>
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
        .baseUrl("http://englishtrain")
        .client(client)
        .addConverterFactory(moshiConverterFactory)
        .build()

    val api = retrofit.create(Api::class.java)

    val response = api.getAccounts()

    response.forEach { println("${it.id} ${it.email} ${it.password} ${it.username} ${it.createdAt}" ) }

}