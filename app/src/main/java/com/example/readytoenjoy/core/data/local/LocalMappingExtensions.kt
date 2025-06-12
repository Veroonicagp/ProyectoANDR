package com.example.readytoenjoy.core.data.local.database

import android.net.Uri
import com.example.readytoenjoy.core.data.local.database.activity.ActivityEntity
import com.example.readytoenjoy.core.data.local.database.adven.AdvenEntity
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.model.Adven

fun ActivityEntity.toLocalModel(): Activity {
    return Activity(
        id = this.id,
        title = this.title,
        location = this.location,
        price = this.price,
        description = this.description,
        advenId = this.advenId,
        img = this.img?.let { Uri.parse(it) }
    )
}

fun Activity.toLocalEntity(): ActivityEntity {
    return ActivityEntity(
        id = this.id,
        title = this.title,
        location = this.location,
        price = this.price,
        description = this.description,
        advenId = this.advenId,
        img = this.img?.toString()
    )
}

fun AdvenEntity.toLocalModel(): Adven {
    return Adven(
        id = this.id,
        name = this.name,
        email = this.email,
        media = this.media?.let { Uri.parse(it) }
    )
}

fun Adven.toLocalEntity(): AdvenEntity {
    return AdvenEntity(
        id = this.id,
        name = this.name,
        email = this.email,
        media = this.media?.toString()
    )
}