package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.models.Comment
import javax.inject.Inject

class GetConditionalCommentsUseCase @Inject constructor() :
    BaseUseCase<GetConditionalCommentsUseCase.Params, List<Comment>>() {

    override suspend fun execute(params: Params): List<Comment> {
        val comments = mutableListOf<Comment>()
        comments.add(Comment("Carlos", "akjdlasdlfkjsadkfjskñl"))
        comments.add(
            Comment(
                "María",
                "akjdlasdlfkjsadkfjskñladskadlksofk aopodjf ajs jfaiefiopsej ifae iojgio jiji aj g`je gajasojpeagj`p"
            )
        )
        comments.add(
            Comment(
                "Ldlakd",
                "akjdlasdlfkjsadkfjskñl  ñskfjdashfjs ads hf adhfhasd ohaopdh"
            )
        )
        comments.add(Comment("Rosalía", "dajkajkdjad"))
        comments.add(
            Comment(
                "Coordenada",
                "akjdlasdlfkjsadkfjskñlalsdfhnsdhfzshdlgsdfhglkjzshgkjhskdhgkzshkjghzskhgdkzhsgjk"
            )
        )
//        return emptyList()
        return comments
    }

    data class Params(
        val id: String,
    )
}