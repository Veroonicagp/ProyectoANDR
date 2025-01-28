package com.example.readytoenjoy.core.data.adven

import com.example.readytoenjoy.core.data.activity.Activity
import com.example.readytoenjoy.core.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.network.activity.ActivityResponse
import com.example.readytoenjoy.core.network.adevn.AdvenResponseLR
import com.example.readytoenjoy.core.network.adevn.LoginRequest
import com.example.readytoenjoy.core.network.adevn.User
import org.intellij.lang.annotations.Identifier
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton



fun AdvenResponseLR.toExternal(): User {
    return User(
        //jwt= this.jwt,
        id = this.user.id,
        name = this.user.name,
        email =  this.user.email,
    )
}
@Singleton
class LoginRepository @Inject constructor(private val api: ReadyToEnjoyApiService) {


    suspend fun login(identifier:String,password:String):String? {
        val response = api.login(
            LoginRequest(identifier,password)
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