package com.leventsurer.manager.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.leventsurer.manager.data.repository.*
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.PREFERENCES_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    //Auth module
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth =FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl) : AuthRepository = impl

    //Firestore module
    @Singleton
    @Provides
    fun provideDatabaseRepository(impl: DatabaseRepositoryImpl) : DatabaseRepository = impl

    @Provides
    @Singleton
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireStoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    /*@Singleton
    @Provides
    fun provideFirebaseStroageInstance(): StorageReference {
        return FirebaseStorage.getInstance().getReference(FirebaseStorageConstants.ROOT_DIRECTORY)
    }*/

    //Shared Preferences
    @Provides
    @Singleton
    fun provideSharedPreferences(
        sharedPrefManager: SharedPrefManager
    ): SharedPreferences = sharedPrefManager.getSharedPref()
}