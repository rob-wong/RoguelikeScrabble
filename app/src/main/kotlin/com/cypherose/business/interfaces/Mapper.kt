package com.cypherose.business.interfaces

interface Mapper<P, T> {
    fun map(param: P): T
}
