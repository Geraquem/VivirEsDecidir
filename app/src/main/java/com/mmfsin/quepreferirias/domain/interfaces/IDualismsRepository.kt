package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.DualismVotedDTO
import com.mmfsin.quepreferirias.data.models.SendDualismDTO
import com.mmfsin.quepreferirias.domain.models.Dualism
import com.mmfsin.quepreferirias.domain.models.DualismFav
import com.mmfsin.quepreferirias.domain.models.DualismVotes

interface IDualismsRepository {
    suspend fun getDualisms(): List<Dualism>
//    suspend fun getDilemmaById(dilemmaId: String): Dilemma?
//
    suspend fun getDualismVotes(dualismId: String): DualismVotes?
    suspend fun voteDualism(dualismId: String, isTop: Boolean, voted: DualismVotedDTO)
//    suspend fun alreadyDilemmaVoted(dilemmaId: String): Boolean?
//
//    suspend fun getDilemmaComments(dilemmaId: String): List<Comment>
//    suspend fun getDilemmaCommentsFromRealm(): List<Comment>
//    suspend fun sendDilemmaComment(dilemmaId: String, comment: CommentDTO): Boolean
//    suspend fun alreadyCommentVoted(commentId: String, vote: CommentVote): CommentAlreadyVoted
//    suspend fun voteDilemmaComment(
//        dilemmaId: String,
//        commentId: String,
//        likes: Long,
//        vote: CommentVote
//    )
//
    suspend fun getFavDualisms(): List<DualismFav>
//    suspend fun setFavDilemma(dilemma: DilemmaFavDTO)
//    suspend fun deleteFavDilemma(dilemmaId: String)
    suspend fun checkIfDualismIsFav(dualismId: String): Boolean

    //
    suspend fun sendDualism(dualism: SendDualismDTO)
//    suspend fun getMyDilemmas(): List<SendDilemma>
//    suspend fun deleteMyDilemma(dilemmaId: String)
//
//    suspend fun getOtherUserDilemmas(userId: String): List<SendDilemma>

    suspend fun reportDualism(dualismId: String)
}