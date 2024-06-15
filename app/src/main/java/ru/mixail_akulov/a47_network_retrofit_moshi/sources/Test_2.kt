package ru.mixail_akulov.a47_network_retrofit_moshi.sources

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class CategoryApiModel (
    @SerializedName("id") @Expose
    var id: Int? = null,

    @SerializedName("name") @Expose
    var name: String? = null
)

interface ApiInterface {

    @GET("getCategories.php")
    fun getCategories(): Call<List<CategoryApiModel>>

}

class ApiClient private constructor(){

    companion object {
        private const val BASE_URL = "http://shop"

        private var apiClient: ApiClient? = null
        private var retrofit: Retrofit? = null

        val instance: ApiClient?
            @Synchronized get() {
                if (apiClient == null) {
                    apiClient = ApiClient()
                }
                return apiClient
            }
    }

    init {
        retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        println("retrofit init")
    }

    val api: ApiInterface
        get() = retrofit!!.create(ApiInterface::class.java)
 }

fun main() {
    val callCategories = ApiClient.instance?.api?.getCategories()

    println("getCategories____${callCategories}")

    callCategories?.enqueue(object: Callback<List<CategoryApiModel>> {
        override fun onResponse(
            call: Call<List<CategoryApiModel>>,
            response: Response<List<CategoryApiModel>>
        ) {
            println("---------------")
            val loadCategories = response.body()

            println("---------------")
            println(loadCategories.toString())
            println("---------------")

        }

        override fun onFailure(call: Call<List<CategoryApiModel>>, t: Throwable) {
            println("Exception")
        }
    })
}