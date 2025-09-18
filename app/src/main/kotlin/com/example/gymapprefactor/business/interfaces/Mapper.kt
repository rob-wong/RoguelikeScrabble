package com.example.gymapprefactor.business.interfaces

interface Mapper<P, T> {
    fun map(param: P): T
}
