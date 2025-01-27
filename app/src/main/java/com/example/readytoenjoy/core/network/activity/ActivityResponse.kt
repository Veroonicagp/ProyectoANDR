package com.example.readytoenjoy.core.network.activity

data class ActivityAttributesResponse(
    val title: String,
    val location: String,
    val price: String,
    val description: String
)

data class ActivityResponse(
    val id: String,
    val attributes: ActivityAttributesResponse
    /**
    val title: String,
    val location: String,
    val price: String,
    val description: String,*/
)
data class ActivityRequest(
    val id: String,
    val title: String,
    val location: String,
    val price: String,
    val description: String,
)
data class ActivityListRawResponse(
    /**val count: String,
    val prev: String?,
    val next: String?,*/
    val data: List<ActivityResponse>
)

data class ActivityRawResponse(
    val data: ActivityResponse
)
