package com.plcoding.jetpackcomposepokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController

import com.plcoding.jetpackcomposepokedex.PackmonList.PackmonListScreen
import com.plcoding.jetpackcomposepokedex.ui.theme.JetpackComposePokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposePokedexTheme {
                val navController= rememberNavController()
                NavHost(navController = navController, startDestination = "pacmon_list_screen" )
                {
                    composable("pacmon_list_screen")
                    {
                        PackmonListScreen(navController=navController)
                    }

                    composable("pacmon_datiles_screen/{domainColor}/{pokemonName}", arguments = listOf(
                        navArgument("domainColor")
                        {
                            type=NavType.IntType
                        },
                        navArgument("pokemonName")
                        {
                            type=NavType.StringType
                        },

                    ))
                    {
                        val domaitColor = remember {
                            val color= it.arguments?.getInt("domainColor")
                            color?.let { Color(it) } ?:Color.White
                        }

                        val PackmonName= remember {
                            it.arguments?.getString("pokemonName")
                        }
                    }
                }
            }
        }
    }

}
