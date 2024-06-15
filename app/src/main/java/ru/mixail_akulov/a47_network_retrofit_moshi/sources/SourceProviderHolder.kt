package ru.mixail_akulov.a47_network_retrofit_moshi.sources

import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.mixail_akulov.a47_network_retrofit_moshi.app.Const
import ru.mixail_akulov.a47_network_retrofit_moshi.app.Singletons
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.SourcesProvider
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.settings.AppSettings
import ru.mixail_akulov.a47_network_retrofit_moshi.sources.base.RetrofitConfig
import ru.mixail_akulov.a47_network_retrofit_moshi.sources.base.RetrofitSourcesProvider

object SourceProviderHolder {

    val sourcesProvider: SourcesProvider by lazy {
        val moshi = Moshi.Builder().build()
        val config = RetrofitConfig(
            retrofit = createRetrofit(moshi),
            moshi = moshi
        )
        RetrofitSourcesProvider(config)
    }

    /**
     * Создайте экземпляр клиента Retrofit.
     */
    private fun createRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * Создайте экземпляр OkHttpClient с перехватчиками для авторизации и журналирования.
     * (see [createAuthorizationInterceptor] and [createLoggingInterceptor]).
     */
    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createAuthorizationInterceptor(Singletons.appSettings))
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    /**
     * Добавьте заголовок авторизации в каждый запрос, если существует JWT-токен.
     */
    private fun createAuthorizationInterceptor(settings: AppSettings): Interceptor {
        return Interceptor { chain ->
            val newBuilder = chain.request().newBuilder()
            val token = settings.getCurrentToken()
            if (token != null) {
                newBuilder.addHeader("Authorization", token)
            }
            return@Interceptor chain.proceed(newBuilder.build())
        }
    }

    /**
     * Записывать запросы и ответы в LogCat.
     */
    private fun createLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

}
