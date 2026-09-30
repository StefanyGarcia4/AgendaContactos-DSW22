package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.ContactDatabase
import com.example.data.repository.ContactRepository
import com.example.ui.screens.ContactScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ContactViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicialización de la capa de persistencia Room y Repositorio
        val database = ContactDatabase.getDatabase(applicationContext)
        val repository = ContactRepository(database.contactDao())

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ContactViewModel = viewModel(
                        factory = ContactViewModel.Factory(repository)
                    )
                    ContactScreen(viewModel = viewModel)
                }
            }
        }
    }
}
