package com.example.readytoenjoy.core.data.network.user

import com.example.readytoenjoy.core.data.local.user.UserLocal
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthenticationInterceptor @Inject constructor(
    private val userLocalDatasource: UserLocal
):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        if (chain.request().method == "POST" && chain.request().url.encodedPath == "/api/auth/local") {
            return chain.proceed(chain.request())
        }
        val token: String? =
                runBlocking {
                    userLocalDatasource.getUser()?.let {
                    return@runBlocking it.token
                }
                    return@runBlocking null
                }

        android.util.Log.d("AuthInterceptor", "Using token: $token")
        token?.let {
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization","Bearer $it")
                .build()
            return chain.proceed(newRequest)
        }

        return chain.proceed(chain.request())



    }
}