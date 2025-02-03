package com.example.readytoenjoy.core.data.adven

import com.example.readytoenjoy.core.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.network.adevn.userResponseLR
import com.example.readytoenjoy.core.network.adevn.LoginRequest
import com.example.readytoenjoy.core.network.adevn.User
import javax.inject.Inject
import javax.inject.Singleton

fun userResponseLR.toExternal(): User {
    return User(
        id = this.user.id,
        name = this.user.name,
        email =  this.user.email,
        advenId = this.user.advenId
    )
}

@Singleton
class LoginRepository @Inject constructor(private val api: ReadyToEnjoyApiService) {
    suspend fun login(identifier:String,password:String):String? {
        val response = api.login(
            LoginRequest(identifier, password)
        )
        // Me he logueado
        return if (response.isSuccessful) {
            // TODO GUARDAR LOCALMENTE EL TOKEN
            response.body()!!.jwt
        }
        // No me he logueado
        else {
            null
        }
    }
}