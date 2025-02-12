package com.example.readytoenjoy.core.data.network.adevn.model

import com.example.readytoenjoy.core.model.User

data class AdvenListRawResponse(
    val data: List<AdvenResponseLR>
)

data class AdvenRawResponse(
    val data: AdvenResponseLR
)

data class AdvenResponseLR(
    val id: String,
    val attributes: AdvenAttributesResponse
)
data class AdvenAttributesResponse(
    val name: String,
    val email: String,
)

data class LoginRequest(
    val identifier: String,
    val password: String
)


data class AdvenRequest(
    val data: AventureroData
)

data class AventureroData(
    val name:String,
    val email: String,
    val password: String,
    val userId: String//relacion con el usuario creado
)

//USER

data class userResponseLR(
    val jwt: String,
    val user: User
)


data class UserRequest(
    val username:String,
    val email: String,
    val password: String,
)


