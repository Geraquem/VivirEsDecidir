package com.mmfsin.quepreferirias.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.*
import com.mmfsin.quepreferirias.domain.interfaces.IRepository
import com.mmfsin.quepreferirias.domain.models.DataDTO
import java.util.UUID

class FirebaseRepository(private val listener: IRepository) {

    fun getDataFromFirebase() {
        Firebase.database.reference.child(ROOT).get().addOnSuccessListener {
            val pruebas = mutableListOf<DataDTO>()
            for (prueba in it.children) {
                val id = UUID.randomUUID().toString()
                val textA = prueba.child(TEXT_TOP).value.toString()
                val textB = prueba.child(TEXT_BOTTOM).value.toString()
                val votesA = prueba.child(YES).childrenCount
                val votesB = prueba.child(NO).childrenCount
                pruebas.add(DataDTO(id, textA, textB, votesA, votesB))
            }
            listener.getDataFromFirebase(pruebas)

        }.addOnFailureListener { listener.somethingWentWrong() }
    }
}