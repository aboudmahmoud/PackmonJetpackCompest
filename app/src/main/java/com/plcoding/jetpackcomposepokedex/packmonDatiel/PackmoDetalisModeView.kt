package com.plcoding.jetpackcomposepokedex.packmonDatiel

import androidx.lifecycle.ViewModel
import com.plcoding.jetpackcomposepokedex.data.remote.response.Packmon
import com.plcoding.jetpackcomposepokedex.repostory.PackmonRepotry
import com.plcoding.jetpackcomposepokedex.util.Resosre
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PackmoDetalisModeView @Inject constructor(private val repostory:PackmonRepotry): ViewModel() {

    suspend fun getPokemonInfo(PakmonName:String):Resosre<Packmon>{
        return repostory.getPacmonInfo(PakmonName)
    }
}