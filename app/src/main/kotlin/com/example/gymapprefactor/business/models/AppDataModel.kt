package com.example.gymapprefactor.business.models

import javax.inject.Singleton

@Singleton
object AppDataModel {
    lateinit var letterList: MutableList<Letter>

    fun initData() {
        fetchLetters()
    }

    private fun fetchLetters() {
        letterList = TemporaryDataFill.letterList.toMutableList()
    }
}
