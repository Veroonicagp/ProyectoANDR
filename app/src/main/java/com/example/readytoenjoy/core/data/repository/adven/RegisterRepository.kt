package com.example.readytoenjoy.core.data.repository.adven

import android.content.Context
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.adevn.model.UserRequest
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRequest
import com.example.readytoenjoy.core.data.network.adevn.model.AventureroData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RegisterRepository @Inject constructor(@ApplicationContext val context: Context,
                                             private val api: ReadyToEnjoyApiService
) {

    suspend fun register(username: String, email: String, password: String): Boolean {
        val userResponse = api.register(
            UserRequest(username, email, password)
        )

        if (!userResponse.isSuccessful) {
            return false
        }

        val userId = userResponse.body()?.user?.id ?: return false

        val aventureroRequest = AdvenRequest(
            AventureroData(
                name = username,
                email = email,
                password = password,
                user = userId
            )
        )

        val aventureroResponse = api.registerAdven(aventureroRequest)
        return aventureroResponse.isSuccessful
    }


}