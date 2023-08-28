package com.salmakhd.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val number: String
)

// must be stored in a database in fact
val userStorage = mutableListOf<User>(
    User(
        id = "100",
        name = "سلما",
        number = "09128584364"
    )
)