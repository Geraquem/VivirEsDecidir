package com.mmfsin.quepreferirias.presentation.send.interfaces

interface ISendDataResultListener {
    fun sendAnother()
    fun retry()
    fun close()
}