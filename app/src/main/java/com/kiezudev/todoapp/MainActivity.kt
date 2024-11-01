package com.kiezudev.todoapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.kiezudev.todoapp.LoginPage
import com.kiezudev.todoapp.RegisterPage
import com.kiezudev.todoapp.TodoApp

private lateinit var auth:FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var startDestination:String
                    auth = Firebase.auth
                    val currentUser = auth.currentUser

                    if (currentUser == null){
                        startDestination = "login"
                    }else{
                        startDestination = "home"
                    }


                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginPage(navController = navController) }
                        composable("register") { RegisterPage(navController = navController) }
                        composable("home") { TodoApp(navController = navController) }
                    }
                }
            }
        }
    }
}
