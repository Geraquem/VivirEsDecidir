package com.mmfsin.quepreferirias.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.*
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.data.models.DataDTO
import javax.inject.Inject

class DataRepository @Inject constructor() : IDataRepository{

//    fun getDataFromFirebase() {
//        Firebase.database.reference.child(PRUEBAS_ROOT).get().addOnSuccessListener {
//            val pruebas = mutableListOf<DataDTO>()
//            for (prueba in it.children) {
//                prueba.key?.let { id ->
//                    val textA = prueba.child(TEXT_TOP).value.toString()
//                    val textB = prueba.child(TEXT_BOTTOM).value.toString()
//                    val votesA = prueba.child(YES).childrenCount
//                    val votesB = prueba.child(NO).childrenCount
//                    val creatorName = prueba.child(CREATOR_NAME).value
//                    if (creatorName != null && creatorName.toString().isNotEmpty()) {
//                        pruebas.add(
//                            DataDTO(id, textA, textB, votesA, votesB, creatorName.toString())
//                        )
//                    } else pruebas.add(DataDTO(id, textA, textB, votesA, votesB))
//
//                }
//            }
////            if (pruebas.isEmpty()) listener.somethingWentWrong()
////            else listener.getDataFromFirebase(pruebas.shuffled())
//
//        }.addOnFailureListener {} //listener.somethingWentWrong() }
//    }
//
//    fun setVote(key: String, voted: String) {
//        Firebase.database.reference.child(PRUEBAS_ROOT).child(key).child(voted).push()
//            .setValue(true)
//    }

    override fun getDataFromFirebase(dataList: List<DataDTO>) {
        TODO("Not yet implemented")
    }

    override fun somethingWentWrong() {
        TODO("Not yet implemented")
    }
}