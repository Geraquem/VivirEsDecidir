package com.mmfsin.quepreferirias.di

import com.mmfsin.quepreferirias.data.database.RealmDatabase
import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.CommentReplyDTO
import com.mmfsin.quepreferirias.data.models.CommentVotedDTO
import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.DilemmaVotedDTO
import com.mmfsin.quepreferirias.data.models.DualismFavDTO
import com.mmfsin.quepreferirias.data.models.DualismVotedDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.data.models.SendDualismDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@Module
@InstallIn(ViewModelComponent::class, ServiceComponent::class)
object RealmDatabaseModule {

    @Provides
    fun provideRealmDatabase(): IRealmDatabase {
        val config = RealmConfiguration.create(
            schema = setOf(
                CommentDTO::class,
                CommentReplyDTO::class,
                CommentVotedDTO::class,
                DilemmaFavDTO::class,
                DilemmaVotedDTO::class,
                DualismFavDTO::class,
                DualismVotedDTO::class,
                SendDilemmaDTO::class,
                SendDualismDTO::class,
                SessionDTO::class
            )
        )

        val realm = Realm.open(config)
        return RealmDatabase(realm)
    }
}