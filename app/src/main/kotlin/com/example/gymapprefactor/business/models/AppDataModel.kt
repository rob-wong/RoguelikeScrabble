package com.example.gymapprefactor.business.models

import javax.inject.Singleton

@Singleton
object AppDataModel {
    lateinit var user: User

    fun initData() {
        fetchUser()
    }

    private fun fetchUser() {
        user = TemporaryDataFill.user
    }
}
