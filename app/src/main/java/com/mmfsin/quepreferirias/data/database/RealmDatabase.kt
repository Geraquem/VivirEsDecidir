package com.mmfsin.quepreferirias.data.database

import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.deleteFromRealm

class RealmDatabase(private val realmConfiguration: RealmConfiguration) : IRealmDatabase {

    private fun getRealm(): Realm {
        return try {
            Realm.getInstance(realmConfiguration)
        } catch (e: IllegalArgumentException) {
            deleteAllData()
            Realm.getInstance(realmConfiguration)
        }
    }

    override fun deleteAllData() {
        val realm = getRealm()
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
        realm.close()
    }

    override fun <O : RealmResults<I>, I : RealmModel> getObjectsFromRealm(action: Realm.() -> O): List<I> {
        val realm = getRealm()
        val results = action(realm)
        val list = results.map { realm.copyFromRealm(it) }
        realm.close()
        return list
    }

    override fun <I : RealmModel> getObjectFromRealm(
        model: Class<I>,
        idName: String,
        id: String
    ): I? {
        val realm = getRealm()
        val obj = realm.where(model).equalTo(idName, id).findFirst()
        val r = obj?.let { realm.copyFromRealm(it) }
        realm.close()
        return r
    }

    override fun <T : RealmModel> addObject(action: () -> T) {
        val realm = getRealm()
        val realmModel = action()
        realm.beginTransaction()
        realm.insertOrUpdate(realmModel)
        realm.commitTransaction()
        realm.close()
    }

    override fun <T : RealmModel> isObjectSaved(
        model: Class<T>,
        idName: String,
        id: String
    ): Boolean {
        val realm = getRealm()
        realm.beginTransaction()
        val obj = realm.where(model).equalTo(idName, id).findFirst()
        realm.commitTransaction()
        realm.close()
        return (obj != null)
    }

    override fun <T : RealmModel> deleteObject(model: Class<T>, idName: String, id: String) {
        val realm = getRealm()
        realm.beginTransaction()
        val obj = realm.where(model).equalTo(idName, id).findFirst()
        if (obj != null) {
            obj.deleteFromRealm()
            realm.commitTransaction()
            realm.close()
        } else realm.cancelTransaction()
    }

    override fun <T : RealmModel> deleteAllObjects(action: Class<T>) {
        val realm = getRealm()
        realm.beginTransaction()
        realm.delete(action)
        realm.commitTransaction()
        realm.close()
    }
}