package com.sliideusers.ui.user.viewmodel

interface UsersActions {
    fun onAddUser(name: String, email: String)
    fun onAddUserDialogDismissed()
    fun onDeleteConfirmed(userId: Int)
    fun onErrorRetry()
    fun onFabClick()
    fun onSnackDisplayed()

    companion object {
        val DEFAULT = object : UsersActions {
            override fun onAddUser(name: String, email: String) = Unit
            override fun onAddUserDialogDismissed() = Unit
            override fun onDeleteConfirmed(userId: Int) = Unit
            override fun onErrorRetry() = Unit
            override fun onFabClick() = Unit
            override fun onSnackDisplayed() = Unit
        }
    }
}