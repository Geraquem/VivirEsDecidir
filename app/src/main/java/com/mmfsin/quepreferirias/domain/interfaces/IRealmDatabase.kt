package com.mmfsin.quepreferirias.domain.interfaces

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

interface IRealmDatabase {
    fun <O : RealmResults<I>, I : RealmModel> getObjectsFromRealm(action: Realm.() -> O): List<I>
    fun <T : RealmModel> addObject(action: () -> T)
    fun <T : RealmModel> deleteObject(action: Realm.() -> T, idName: String, id: String)
    fun <T : RealmModel> deleteAllObjects(action: Class<T>)
    fun deleteAllData()
}