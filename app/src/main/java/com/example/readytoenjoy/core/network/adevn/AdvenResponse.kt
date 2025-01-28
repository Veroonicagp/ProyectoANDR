package com.example.readytoenjoy.core.network.adevn

data class AdvenRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
)

data class AdvenAttributesResponse(
    val name: String,
    val surname: String,
    val email: String,
)

data class AdvenListRawResponse(
    val data: List<AdvenResponse>
)

data class AdvenRawResponse(
    val data: AdvenResponse
)

data class AdvenResponse(
    val id: String,
    val attributes: AdvenAttributesResponse
)

data class LoginRequest(
    val identifier: String,
    val password: String
)

data class AdvenResponseLR(
    val jwt: String,
    val user: User
)

data class User(
    val id: String,
    val name: String,
    val email: String
)


