package com.mefy.platemate.di

import android.content.Context
import androidx.room.Room
import com.mefy.platemate.data.local.room.PlateMateDatabase
import com.mefy.platemate.data.local.room.dao.RecentSearchDao
import com.mefy.platemate.data.local.room.dao.SavedPlateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatabaseModule {

    @Provides
    @Singleton
    fun providePlateMateDatabase(
        @ApplicationContext context: Context
    ): PlateMateDatabase = Room.databaseBuilder(
        context,
        PlateMateDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(PlateMateDatabase.MIGRATION_1_2)
        .build()

    @Provides
    fun provideRecentSearchDao(
        database: PlateMateDatabase
    ): RecentSearchDao = database.recentSearchDao()

    @Provides
    fun provideSavedPlateDao(
        database: PlateMateDatabase
    ): SavedPlateDao = database.savedPlateDao()

    private const val DATABASE_NAME = "plate_mate.db"
}
