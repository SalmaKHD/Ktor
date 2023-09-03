package com.salmakhd.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val note: String
)
