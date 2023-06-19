package com.mmfsin.quepreferirias.base

abstract class BaseUseCaseNoParams<T> {
    abstract suspend fun execute(): T
}