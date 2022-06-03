package com.plcoding.jetpackcomposepokedex.repostory

import com.plcoding.jetpackcomposepokedex.data.remote.PackmonApi
import com.plcoding.jetpackcomposepokedex.data.remote.response.Packmon
import com.plcoding.jetpackcomposepokedex.data.remote.response.PackmonList
import com.plcoding.jetpackcomposepokedex.util.Resosre
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PackmonRepotry @Inject constructor(
    private val api: PackmonApi
) {

    suspend fun getPacmonList(limit: Int, offest: Int): Resosre<PackmonList> {
        val response = try {
            api.getPackmonList(limit, offest)
        } catch (e: Exception) {
            return Resosre.error("Error oucerd ${e.message}")
        }
        return Resosre.succses(response)
    }

    suspend fun getPacmonInfo(PackmonName:String): Resosre<Packmon> {
        val response = try {
            api.getPokmenInfo(PackmonName)
        } catch (e: Exception) {
            return Resosre.error("Error oucerd ${e.message}")
        }
        return Resosre.succses(response)
    }
}