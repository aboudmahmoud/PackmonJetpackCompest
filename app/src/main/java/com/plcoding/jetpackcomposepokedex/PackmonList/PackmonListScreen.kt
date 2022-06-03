package com.plcoding.jetpackcomposepokedex.PackmonList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Alignment.Companion.Center

import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState

import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import coil.request.ImageRequest
import com.google.accompanist.coil.CoilImage
import com.plcoding.jetpackcomposepokedex.R
import com.plcoding.jetpackcomposepokedex.data.Models.PokeIndxListEntry
import com.plcoding.jetpackcomposepokedex.ui.theme.RobotoCondensed

@Composable
fun PackmonListScreen(navController: NavController,viewModel: PackmonListViewModel= hiltNavGraphViewModel()) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Packmon", modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SerchBar(
                hint = "serach...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                ) {
                viewModel.SerachPokmenList(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            PackmonList(navController = navController)
        }


    }
}

@Composable
fun SerchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSerach: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var IsHindDispaly by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSerach(it)
            },
            maxLines = 1, singleLine = true, textStyle = TextStyle(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    IsHindDispaly = it != FocusState.Active && text.isNotEmpty()
                },

            )

        if (IsHindDispaly) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun PackmonList(
    navController: NavController,
    viewModel: PackmonListViewModel = hiltNavGraphViewModel()
) {
    val pokemonList by remember { viewModel.pokmenList }
    val endReach by remember {
        viewModel.endReach
    }
    val LoadError by remember {
        viewModel.loadError
    }
    val isLoading by remember {
        viewModel.IsLoading
    }
    val isSeraching by remember {
        viewModel.IsSeraching
    }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            pokemonList.size / 2 + 1
        }
        items(itemCount) {
            if (it >= itemCount - 1 && !endReach && !isLoading && !isSeraching) {
                viewModel.LoadPacmonPaginated()
            }
            PokeDiexRow(rowIndex = it, entries = pokemonList, navController = navController)
        }

    }
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    )
    {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (LoadError.isNotEmpty()) {
            RetrySection(error = LoadError) {
                viewModel.LoadPacmonPaginated()
            }
        }
    }
}

@Composable
fun PokeiexEntary(
    entry: PokeIndxListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PackmonListViewModel = hiltNavGraphViewModel()
) {
    var defaultDominant = MaterialTheme.colors.surface
    var domainColor by remember {
        mutableStateOf(defaultDominant)
    }
    Box(contentAlignment = Center, modifier = modifier
        .shadow(
            5.dp,
            RoundedCornerShape(10.dp)
        )
        .clip(RoundedCornerShape(10.dp))
        .aspectRatio(1f)
        .background(
            Brush.verticalGradient(
                listOf(
                    domainColor,
                    defaultDominant
                )
            )
        )
        .clickable {
            navController.navigate(
                "pacmon_datiles_screen/${domainColor.toArgb()}/${entry.PackmonName}"
            )
        }
    ) {
        Column {
            CoilImage(
                request = ImageRequest.Builder(LocalContext.current)
                    .data(entry.PackmonImagUrl).target {
                        viewModel.calacDomaitColor(it) { color ->
                            domainColor = color

                        }
                    }.build(), contentDescription = entry.PackmonName,
                fadeIn = true, modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.5f)
                )
            }
            Text(
                text = entry.PackmonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PokeDiexRow(
    rowIndex: Int,
    entries: List<PokeIndxListEntry>,
    navController: NavController
) {
    Column {
        Row {
            PokeiexEntary(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(15.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                PokeiexEntary(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetery: () -> Unit
) {
    Column() {
        Text(text = error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetery() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }

}