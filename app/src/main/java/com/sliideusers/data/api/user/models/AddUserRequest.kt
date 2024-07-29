package com.sliideusers.data.api.user.models

data class AddUserRequest(
    val name: String,
    val email: String,
    val gender: String = "male",
    val status: String = "inactive",
)