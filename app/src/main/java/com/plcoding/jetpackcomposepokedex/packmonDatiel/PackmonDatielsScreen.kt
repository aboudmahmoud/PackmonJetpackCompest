package com.plcoding.jetpackcomposepokedex.packmonDatiel


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import com.google.accompanist.coil.CoilImage
import com.plcoding.jetpackcomposepokedex.R
import com.plcoding.jetpackcomposepokedex.data.remote.response.Packmon
import com.plcoding.jetpackcomposepokedex.data.remote.response.Type
import com.plcoding.jetpackcomposepokedex.util.Resosre
import com.plcoding.jetpackcomposepokedex.util.parseStatToAbbr
import com.plcoding.jetpackcomposepokedex.util.parseStatToColor
import com.plcoding.jetpackcomposepokedex.util.parseTypeToColor
import java.lang.Math.round
import java.util.*

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
        PackmonDatielsTopScreen(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )

        PokmenDeatilsWrapper(
            pokemonInfo = packmonInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pakmonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + pakmonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )

        )
        Box(
            modifier = Modifier
                .fillMaxSize(), contentAlignment = Alignment.TopCenter
        ) {
            if (packmonInfo is Resosre.succses) {
                packmonInfo.data?.sprites?.let {
                    CoilImage(
                        data = it.front_default,
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
fun PackmonDatielsTopScreen(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.TopStart, modifier = modifier.background(
            Brush.verticalGradient(
                listOf(
                    Color.Black,
                    Color.Transparent
                )
            )
        )
    ) {
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
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
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,

    ) {
    when (pokemonInfo) {
        is Resosre.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier,

                )
        }
        is Resosre.succses -> {
            PokeDeailsSelaction(
                packmonInfo = pokemonInfo.data!!,
                modifier = modifier.offset(y = (-20).dp)
            )
        }
        is Resosre.error -> {
            Text(
                text = pokemonInfo.Messeg!!,
                color = Color.Red,
                modifier = modifier,

                )
        }
    }
}

@Composable
fun PokeDeailsSelaction(
    packmonInfo: Packmon,
    modifier: Modifier = Modifier,

    ) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            "${packmonInfo.id} ${packmonInfo.name.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
        PackmoneTypeSelction(types = packmonInfo.types)
        PokmoneDatileDataSelction(
            PackmonWeight = packmonInfo.weight,
            PackmonHieht = packmonInfo.height,
        )
        PokmoneBaseState( pokemonInfo= packmonInfo)
    }
}

@Composable
fun PackmoneTypeSelction(types: List<Type>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        for (type in types) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(parseTypeToColor(type))
                    .height(35.dp)
            ) {
                Text(
                    text = type.type.name.capitalize(Locale.ROOT),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun PokmoneDatileDataSelction(
    PackmonHieht: Int,
    PackmonWeight: Int,
    sectionHight: Dp = 80.dp,


    ) {
    val pakmonWeightInKg = remember {
        round(PackmonWeight * 100f) / 1000f
    }
    val pakmonHiehtInMatters = remember {
        round(PackmonHieht * 100f) / 1000f
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        PokemonDetaiDataitem(
            dataValue = pakmonWeightInKg, dataUnit = "Kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHight)
                .background(Color.LightGray)
        )
        PokemonDetaiDataitem(
            dataValue = pakmonHiehtInMatters, dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PokemonDetaiDataitem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(painter = dataIcon, contentDescription = null, tint = MaterialTheme.colors.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${dataValue}${dataUnit} ",
            color = MaterialTheme.colors.onSurface,

            )
    }

}


@Composable
fun PockmoneStatus(
    stateName: String,
    starteValue: Int,
    stateMaxValue: Int,
    stateColor: Color,
    hight: Dp = 28.dp,
    animeDirection: Int = 1000,
    animeDelay: Int = 0,

    ) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val CurrentParenst = animateFloatAsState(
        targetValue = if (animationPlayed) {
            starteValue / stateMaxValue.toFloat()
        } else {
            0f
        },
        animationSpec = tween(
            animeDirection,
            animeDelay

        )

    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(hight)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(CurrentParenst.value)
                .clip(CircleShape)
                .background(stateColor)
                .padding(horizontal = 8.dp)
        ){
            Text(text=stateName,
            fontWeight = FontWeight.Bold,)
            Text(text=(CurrentParenst.value * stateMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold,)

        }
    }
}

@Composable
fun PokmoneBaseState(
    pokemonInfo: Packmon,
    animeDelayPerItem: Int=100,

) {
    val maxballeState = remember {
        pokemonInfo.stats.maxOf { it.base_stat }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Text(text = "Base stats" ,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            
            color = MaterialTheme.colors.onSurface)
        Spacer(modifier = Modifier.height(4.dp))

        for(i in pokemonInfo.stats.indices){
            val state= pokemonInfo.stats[i]
            PockmoneStatus(
                stateName = parseStatToAbbr(state),
                starteValue = state.base_stat,
                stateMaxValue = maxballeState,
                stateColor = parseStatToColor(state),
                animeDelay = i * animeDelayPerItem)

        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}