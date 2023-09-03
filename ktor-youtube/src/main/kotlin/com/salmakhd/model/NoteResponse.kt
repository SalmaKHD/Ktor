package com.salmakhd.model

import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
data class NoteResponse<T> (
    val data: T,
    val success: Boolean
)