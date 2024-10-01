package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.SendDualismDTO

interface IDualismsRepository {
    //    suspend fun getDilemmas(): List<Dilemma>
//    suspend fun getDilemmaById(dilemmaId: String): Dilemma?
//
//    suspend fun getDilemmaVotes(dilemmaId: String): DilemmaVotes?
//    suspend fun voteDilemma(dilemmaId: String, isYes: Boolean, voted: DilemmaVotedDTO)
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
//    suspend fun getFavDilemmas(): List<DilemmaFav>
//    suspend fun setFavDilemma(dilemma: DilemmaFavDTO)
//    suspend fun deleteFavDilemma(dilemmaId: String)
//    suspend fun checkIsDilemmaIsFav(dilemmaId: String): Boolean
//
    suspend fun sendDualism(dualism: SendDualismDTO)
//    suspend fun getMyDilemmas(): List<SendDilemma>
//    suspend fun deleteMyDilemma(dilemmaId: String)
//
//    suspend fun getOtherUserDilemmas(userId: String): List<SendDilemma>
}