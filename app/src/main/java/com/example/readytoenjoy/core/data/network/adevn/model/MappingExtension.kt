package com.example.readytoenjoy.core.data.network.adevn.model

import com.example.readytoenjoy.core.model.Adven

fun userResponseLR.toExternal(): User {
    return User(
        id = this.user.id,
        name = this.user.name,
        email =  this.user.email,
        advenId = this.user.advenId
    )
}

fun AdvenResponseLR.toExternal(): Adven {
    return Adven(
        id = this.id,
        name = this.attributes.name,
        email = this.attributes.email
    )
}

fun List<AdvenResponseLR>.toExternal():List<Adven> {
    return this.map(AdvenResponseLR::toExternal)
}