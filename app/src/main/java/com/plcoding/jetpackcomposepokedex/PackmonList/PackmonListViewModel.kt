package com.plcoding.jetpackcomposepokedex.PackmonList


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.jetpackcomposepokedex.repostory.PackmonRepotry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.palette.graphics.Palette
import com.plcoding.jetpackcomposepokedex.data.Models.PokeIndxListEntry
import com.plcoding.jetpackcomposepokedex.util.Resosre
import com.plcoding.jetpackcomposepokedex.util.constans.Page_Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.util.*

@HiltViewModel
class PackmonListViewModel @Inject constructor(
    private val repotry: PackmonRepotry
) : ViewModel() {
    private var curePage: Int = 0
    var pokmenList =  mutableStateOf<List<PokeIndxListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var IsLoading = mutableStateOf(false)
    var endReach = mutableStateOf(false)

    private  var cahedPokemDex= listOf<PokeIndxListEntry>()
    private var IsSerachingStaring: Boolean = true
     var IsSeraching= mutableStateOf(false)

  public  fun SerachPokmenList(qurey:String){
        val listToSerach=if(IsSerachingStaring){
            pokmenList.value
        }else{
            cahedPokemDex
        }
        viewModelScope.launch(Dispatchers.Default){
            if(qurey.isEmpty()){
                IsSeraching.value=false
                pokmenList.value=cahedPokemDex
                IsSerachingStaring=true
                return@launch
            }
            val result=listToSerach.filter {
                it.PackmonName.contains(qurey.trim(),true)|| it.PackmonNumber.toString() == qurey.trim()
            }
            if (IsSerachingStaring){
                cahedPokemDex=pokmenList.value
                IsSerachingStaring=false
            }
            pokmenList.value=result
            IsSeraching.value=true
        }

    }
    init {
        LoadPacmonPaginated()
    }
    fun LoadPacmonPaginated() {
        viewModelScope.launch {
            IsLoading.value = true
            val result = repotry.getPacmonList(Page_Size, (curePage * Page_Size))
            when (result) {
                is Resosre.succses -> {
                    endReach.value = curePage * Page_Size >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if(entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokeIndxListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())


                    }
                    curePage++
                    loadError.value=""
                    IsLoading.value=false
                 pokmenList.value += pokedexEntries
                }

                is Resosre.error -> {
                    loadError.value=result.Messeg!!
                    IsLoading.value=false
                }


            }
        }
    }

    fun calacDomaitColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorVlaue -> onFinish(Color(colorVlaue)) }
        }
    }
}