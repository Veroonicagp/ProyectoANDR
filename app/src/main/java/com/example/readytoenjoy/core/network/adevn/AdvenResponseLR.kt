package com.example.readytoenjoy.core.network.adevn

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
    val surname: String,
    val email: String,
)

data class LoginRequest(
    val identifier: String,
    val password: String
)

data class AdvenRequest(
    val email: String,
    val password: String,
)

data class userResponseLR(
    val jwt: String,
    val user: User
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val advenId: String,
)


