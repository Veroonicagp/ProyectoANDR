package com.example.readytoenjoy.core.data.network.activity.model

import androidx.core.net.toUri
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.di.NetworkServiceModule

fun ActivityResponse.toModel(): Activity {
    return Activity(
        id = this.id,
        title = this.attributes.title,
        price = this.attributes.price,
        location = this.attributes.location,
        description = this.attributes.description,
        img = "${NetworkServiceModule.STRAPI}${this.attributes.img?.formats?.small?.url}".toUri(),
        advenId = this.attributes.advenId
    )
}
fun List<ActivityResponse>.toModel():List<Activity> = map(ActivityResponse::toModel)

fun Activity.toRemoteModel(): ActivityRequest {
    return ActivityRequest(
        ActivityData(
        title = this.title,
        location = this.location,
        price = this.price,
        description = this.description,
        advenId = this.advenId
    )
    )
}

fun ActivityResponse.toExternal(): Activity {

    return Activity(
        id = this.id,
        title = this.attributes.title,
        price = this.attributes.price,
        location = this.attributes.location,
        description = this.attributes.description,
        img = this.attributes.img?.formats?.small?.url?.toUri(),
        advenId = this.attributes.advenId
    )
}