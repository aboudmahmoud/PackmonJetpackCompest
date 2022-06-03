package com.plcoding.jetpackcomposepokedex.packmonDatiel


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import com.google.accompanist.coil.CoilImage

import com.plcoding.jetpackcomposepokedex.data.remote.response.Packmon
import com.plcoding.jetpackcomposepokedex.util.Resosre

@Composable
fun PokemonDatielScreen(
    domainColor: Color,
    PackmonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pakmonImageSize: Dp = 200.dp,
    viewModel: PackmoDetalisModeView = hiltNavGraphViewModel()
) {
    val packmonInfo = produceState<Resosre<Packmon>>(initialValue = Resosre.Loading()) {

        value = viewModel.getPokemonInfo(PackmonName)
    }.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(domainColor)
            .padding(bottom = 16.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.TopCenter){
            if (packmonInfo is Resosre.succses) {
                packmonInfo.data?.sprites?.let {
               CoilImage(data = it.front_default,
                   contentDescription = packmonInfo.data.name,
               fadeIn = true,
               modifier = Modifier
                   .size(pakmonImageSize)
                   .offset(y = topPadding)

               )
               {
                   
               }
                }
            }
        }
    }
}

@Composable
fun PackmonDatielsTopScreen(navController: NavController,modifier: Modifier=Modifier) {
    Box(contentAlignment = Alignment.TopCenter,modifier=modifier.background(Brush.verticalGradient(
        listOf(
            Color.Black,
            Color.Transparent)
    )) ){
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint=Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable { navController.popBackStack() }


        )
    }
    
}

@Composable
fun PokmenDeatilsWrapper(
    pokemonInfo: Resosre<Packmon>,
    modifier: Modifier= Modifier,
    loadingModifier: Modifier= Modifier,

) {
    
}