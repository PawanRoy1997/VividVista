package com.nextxform.vividvista.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.nextxform.vividvista.data.Wallpapers
import kotlinx.coroutines.flow.MutableStateFlow

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {
    val wallpapersFlow: MutableStateFlow<Wallpapers> = MutableStateFlow(Wallpapers())
    private val database = FirebaseDatabase.getInstance()
    private val gson = Gson()

    fun getWallpapers() {
        database.reference.child("bikerider").get().addOnCompleteListener {
            wallpapersFlow.value = try {
                Log.d(TAG, "getWallpapers: Complete")
                gson.fromJson(it.result.value.toString(), Wallpapers::class.java)
            } catch (e: Exception) {
                Log.d(TAG, "getWallpapers: ${e.message}")
                Wallpapers()
            }
        }.addOnFailureListener {
            Log.d(TAG, "getWallpapers: ${it.message}")
        }
    }
}