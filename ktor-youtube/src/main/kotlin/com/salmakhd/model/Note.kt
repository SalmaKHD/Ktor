package com.salmakhd.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int,
    val note: String
)