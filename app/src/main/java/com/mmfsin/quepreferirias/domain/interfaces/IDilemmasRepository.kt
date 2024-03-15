package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaFav

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

    suspend fun setFavDilemma(dilemma: DilemmaFavDTO)
    suspend fun getFavDilemmas(): List<DilemmaFav>
    suspend fun checkIsDilemmaIsFav(dilemmaId: String): Boolean
}