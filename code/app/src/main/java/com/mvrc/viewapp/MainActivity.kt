package com.mvrc.viewapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mvrc.viewapp.navigation.AuthNavGraph
import com.mvrc.viewapp.presentation.theme.ViewAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity serves as the entry point for the ViewApp. It is annotated with
 * [AndroidEntryPoint] to enable Hilt for dependency injection. The main content
 * of the activity is built using Jetpack Compose.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewAppTheme {
                val appViewModel: AppViewModel = hiltViewModel()
                navController = rememberNavController()

                AuthNavGraph(
                    navController = navController,
                    appViewModel = appViewModel,
                )
            }
        }
    }
}
