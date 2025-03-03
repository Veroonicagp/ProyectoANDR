package com.example.readytoenjoy.core.data.network.adevn.model

import androidx.core.net.toUri
import com.example.readytoenjoy.core.data.network.activity.model.toModel
import com.example.readytoenjoy.core.model.Adven
import com.example.readytoenjoy.core.model.User

fun userResponseLR.toExternal(): User {
    return User(
        id = this.user.id,
        name = this.user.name,
        email =  this.user.email,
        advenId = this.user.advenId,
        token = this.jwt
    )
}

fun AdvenResponseLR.toExternal(): Adven {
    return Adven(
        id = this.id,
        name = this.attributes.name,
        email = this.attributes.email,
        media = "${this.attributes.media?.data?.attributes?.formats?.small?.url}".toUri()
    )
}

fun List<AdvenResponseLR>.toExternal():List<Adven> {
    return this.map(AdvenResponseLR::toExternal)
}