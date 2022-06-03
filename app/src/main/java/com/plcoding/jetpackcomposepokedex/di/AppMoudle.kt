package com.plcoding.jetpackcomposepokedex.di

import com.plcoding.jetpackcomposepokedex.data.remote.PackmonApi
import com.plcoding.jetpackcomposepokedex.repostory.PackmonRepotry
import com.plcoding.jetpackcomposepokedex.util.constans.Base_Url
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppMoudle {

    @Singleton
    @Provides
    fun providPackmonReporty(
        api:PackmonApi
    ) = PackmonRepotry(api)
    @Singleton
    @Provides
    fun prvoidePackmonApi():PackmonApi{
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Base_Url).build().create(PackmonApi::class.java)
    }
}