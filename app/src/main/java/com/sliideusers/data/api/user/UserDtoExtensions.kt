package com.sliideusers.data.api.user

import com.sliideusers.data.api.user.models.UserDto
import com.sliideusers.domain.model.user.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
    )
}