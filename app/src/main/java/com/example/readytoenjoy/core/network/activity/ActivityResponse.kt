package com.example.readytoenjoy.core.network.activity

import android.net.Uri

data class ActivityAttributesResponse(
    val title: String,
    val img: Uri?,
    val location: String,
    val price: String,
    val description: String,
    val advenId: String?=null,
)

data class ActivityResponse(
    val id: String,
    val attributes: ActivityAttributesResponse
    /**val title: String,
    val img: Uri?,
    val location: String,
    val price: String,
    val description: String,
    val advenId: String?=null,**/
)

data class ActivityRequest(
    val id: String,
    val title: String,
    val location: String,
    val price: String,
    val description: String,
    val advenId: String?=null,
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
data class MediaFormats(
    val small: ImageAttributes,
    val thumbnail: ImageAttributes,
)
data class ImageAttributes(
    val url: String
)

//modelo crear

data class CreateActivity(
    val data:CreateActivityPayload
)

// Cuerpo
data class CreateActivityPayload(
    val title: String,
    //val img:Uri?,
    val location: String,
    val price: String,
    val description: String,
    //val advenId: String?=null,
    val latitude: Double?=null,
    val longitude: Double?=null,
)

data class CreatedMediaItemResponse(
    val id:Int,
    val documentId: String,
    val name:String,
    val formats: MediaFormats
)


