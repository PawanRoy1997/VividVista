package com.nextxform.vividvista

import android.app.WallpaperManager
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask.TaskSnapshot
import com.google.firebase.storage.ktx.storage
import com.nextxform.vividvista.data.Wallpapers
import com.nextxform.vividvista.ui.theme.VividVistaTheme
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

private const val TAG = "WallpaperFullScreenActivity"

class WallpaperFullScreenActivity : ComponentActivity() {
    private var storage = Firebase.storage.reference.child("4K")
    private var bitmap: Bitmap? = null
    private val downloadState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VividVistaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val downloadState = downloadState.collectAsState()
                    if (downloadState.value) WallpaperFullScreen(bitmap = bitmap) { setWallpaper() }
                    else WallpaperFullScreen(bitmap = null) {
                        Toast.makeText(
                            this@WallpaperFullScreenActivity,
                            "Downloading wallpaper",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val link = intent.extras?.getString(Intent.EXTRA_TEXT).orEmpty()
        Log.d(TAG, "onCreate: $link")
        val fileName = link.split(".")

        val downloadedFile = File.createTempFile(fileName[0], fileName[1])
        storage.child(link).getFile(downloadedFile).addOnSuccessListener {
            downloadedFile.inputStream().use { stream ->
                bitmap = BitmapFactory.decodeStream(stream)
            }
            downloadState.value = true
            Log.d(TAG, "onCreate: File Downloaded Successfully")
        }.addOnFailureListener {
            Log.d(TAG, "onCreate: Failed to download, ${it.message}")
        }
    }

    private fun setWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(this)
        try {
            if (bitmap != null) wallpaperManager.setBitmap(bitmap)
            Toast.makeText(this, "Operation Successful", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "setWallpaper: Operation Successful")
        } catch (e: Exception) {
            Toast.makeText(this, "Operation Failed", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "setWallpaper: ${e.message}")
        }
    }
}

@Composable
fun WallpaperFullScreen(bitmap: Bitmap?, setWallpaper: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        val painter: Painter =
            if (bitmap == null) painterResource(id = R.drawable.sample) else BitmapPainter(bitmap.asImageBitmap())
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            painter = painter,
            contentDescription = "Wallpaper"
        )
        Button(onClick = { setWallpaper() }) {
            Text("Apply Wallpaper")
        }
    }
}

@Preview
@Composable
fun WallpaperFullScreenPreview() {
    WallpaperFullScreen(null, {})
}