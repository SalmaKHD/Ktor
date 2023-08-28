package com.salmakhd.model

import kotlinx.serialization.Serializable

@Serializable
data class BodyInfo (
    val id: String,
    val gender: String,
    val height: Float,
    val weight: Float,
    val age: Int,
    val stepStride: Float,
    val activityLevel: String
)

val bodyInfoStorage = mutableListOf<BodyInfo>()

