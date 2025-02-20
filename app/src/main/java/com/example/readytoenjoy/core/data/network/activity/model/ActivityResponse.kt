package com.example.readytoenjoy.core.data.network.activity.model

data class ActivityAttributesResponse(
    val title: String,
    val img: Media?,
    val location: String,
    val price: String,
    val description: String,
    val advenId: String?,
)
data class ActivityResponse(
    val id: String,
    val attributes: ActivityAttributesResponse
)
data class ActivityListRawResponse(
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
data class Media(
    val documentId: String,
    val formats: MediaFormats
)

//modelo crear

data class ActivityRequest(
    val data: ActivityData
)

// Cuerpo
data class ActivityData(
    val title: String,
    //val img:Uri?,
    val location: String,
    val price: String,
    val description: String,
    val advenId: String?,
    val latitude: Double?=null,
    val longitude: Double?=null,
)

data class CreatedMediaItemResponse(
    val id:Int,
    val documentId: String,
    val name:String,
    val formats: MediaFormats
)


