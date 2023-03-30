package com.mmfsin.quepreferirias.data.database

import io.realm.RealmConfiguration

object RealmModule {
    fun realmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
    }
}