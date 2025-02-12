package com.example.readytoenjoy.core.data.network.activity.model

import com.example.readytoenjoy.core.model.Activity

fun ActivityResponse.toModel(): Activity {
    return Activity(
        id = this.id,
        title = this.attributes.title,
        price = this.attributes.price,
        location = this.attributes.location,
        description = this.attributes.description,
        //img = this.attributes.img,
        //advenId = this.attributes.advenId
    )
}
fun List<ActivityResponse>.toModel():List<Activity> = map(ActivityResponse::toModel)

fun Activity.toRemoteModel(): CreateActivity {
    return CreateActivity(
        CreateActivityPayload(
        title = this.title,
        location = this.location,
        price = this.price,
        //img = this.img,
        description = this.description,
        //advenId = this.advenId

    )
    )
}