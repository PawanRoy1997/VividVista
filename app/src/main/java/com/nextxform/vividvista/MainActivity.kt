package com.nextxform.vividvista

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.nextxform.vividvista.ui.theme.VividVistaTheme
import com.nextxform.vividvista.viewModels.MainViewModel

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    private var hdStorage = Firebase.storage.reference.child("HD")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VividVistaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel by viewModels<MainViewModel>()
                    val wallpapers by viewModel.wallpapersFlow.collectAsState()

                    viewModel.getWallpapers()

                    Log.d(TAG, "onCreate: ${wallpapers.wallpapers.size}")
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(wallpapers.wallpapers.size) { index ->
                            hdStorage.child(wallpapers.wallpapers[index].hdLink).downloadUrl
                                .WallpaperItem(modifier = Modifier) {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            WallpaperFullScreenActivity::class.java
                                        ).putExtra(
                                            Intent.EXTRA_TEXT,
                                            wallpapers.wallpapers[index].ultraLink
                                        )
                                    )
                                }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Task<Uri>.WallpaperItem(modifier: Modifier, clickListener: () -> Unit) {
    var wallpaperLink by remember { mutableStateOf(Uri.parse("")) }
    addOnSuccessListener {
        wallpaperLink = it
        Log.d(TAG, "WallpaperItem: $it")
    }
    addOnFailureListener {
        Log.d(TAG, "WallpaperItem: Failed to Load $this ${it.message}")
    }
    WallPaperItem(
        modifier = modifier,
        clickListener = clickListener,
        uri = wallpaperLink,
        string = wallpaperLink.lastPathSegment.orEmpty()
    )
}

@Composable
fun WallPaperItem(modifier: Modifier, clickListener: () -> Unit, uri: Uri, string: String) {
    Column(
        modifier
            .width(90.dp)
            .clickable { clickListener.invoke() }
    ) {
        AsyncImage(
            model = uri, contentDescription = "", modifier = Modifier
                .width(90.dp)
                .height(160.dp)
        )
        Text(
            text = string, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun WallPaperItemPreview() {
    VividVistaTheme {
        val name = "Sample Wallpaper"
        WallPaperItem(Modifier, {}, Uri.parse(""), name)
    }
}
