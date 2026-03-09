package com.example.favoritetwittersearches

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.favoritetwittersearches.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DictionaryApp()
                }
            }
        }
    }
}

@Composable
fun DictionaryApp() {

    val context = LocalContext.current

    var searchText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    val dictionaryList = remember { mutableStateListOf<DictionaryEntry>() }

    // Читање на TXT фајлот
    LaunchedEffect(true) {

        val inputStream = context.assets.open("dictionary.txt")
        val lines = inputStream.bufferedReader().readLines()

        for (line in lines) {
            val parts = line.split(",")

            if (parts.size == 2) {

                val english = parts[0].trim()
                val macedonian = parts[1].trim()

                dictionaryList.add(
                    DictionaryEntry(
                        english = english,
                        macedonian = macedonian
                    )
                )
            }
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "Macedonian - English Dictionary",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter word (English or Macedonian)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

                val found = dictionaryList.find {

                    it.english.equals(searchText, ignoreCase = true) ||
                            it.macedonian.equals(searchText, ignoreCase = true)

                }

                resultText = if (found != null) {
                    "${found.english} = ${found.macedonian}"
                } else {
                    "Word not found in dictionary"
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = resultText,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "All Dictionary Words",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {

            items(dictionaryList) { word ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(text = word.english)

                        Text(text = word.macedonian)

                    }
                }
            }
        }
    }
}