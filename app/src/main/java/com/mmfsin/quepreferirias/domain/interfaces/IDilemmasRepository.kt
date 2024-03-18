package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.SendDilemma

interface IDilemmasRepository {
    /** DILEMMAS */
    suspend fun getDilemmas(): List<Dilemma>
    suspend fun getDilemmaComments(dilemmaId: String): List<Comment>
    suspend fun getDilemmaCommentFromRealm(): List<Comment>
    suspend fun sendDilemmaComment(dilemmaId: String, comment: CommentDTO): Boolean
    suspend fun voteDilemmaComment(
        dilemmaId: String,
        commentId: String,
        likes: Long,
        vote: CommentVote
    )

    suspend fun getFavDilemmas(): List<DilemmaFav>
    suspend fun setFavDilemma(dilemma: DilemmaFavDTO)
    suspend fun deleteFavDilemma(dilemmaId: String)
    suspend fun checkIsDilemmaIsFav(dilemmaId: String): Boolean

    suspend fun sendDilemma(dilemma: SendDilemmaDTO): Boolean
}