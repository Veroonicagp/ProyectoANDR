package com.example.readytoenjoy.core.network.activity

import androidx.core.net.toUri
import com.example.readytoenjoy.core.data.activity.Activity

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

fun Activity.toRemoteModel():CreateActivity {
    return CreateActivity(CreateActivityPayload(
        title = this.title,
        location = this.location,
        price = this.price,
        //img = this.img,
        description = this.description,
        //advenId = this.advenId

    ))
}