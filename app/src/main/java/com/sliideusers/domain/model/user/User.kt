package com.sliideusers.domain.model.user

data class User(
    val id: Int,
    val name: String,
    val email: String,
) {
    val initials: String = name
        .trim()  // Remove leading and trailing spaces
        .split(Regex("\\s+"))  // Split by one or more spaces
        .filter { it.isNotEmpty() }  // Remove any empty parts
        .map { it[0] }
        .take(3)
        .joinToString("")
        .uppercase()
}