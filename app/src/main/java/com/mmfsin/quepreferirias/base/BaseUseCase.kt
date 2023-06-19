package com.mmfsin.quepreferirias.base

abstract class BaseUseCase<params, T> {
    abstract suspend fun execute(params: params): T
}