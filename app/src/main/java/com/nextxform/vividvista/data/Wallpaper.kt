package com.nextxform.vividvista.data

import com.google.gson.annotations.SerializedName

data class Wallpapers(
    @SerializedName("wallpaper") val wallpapers: List<Wallpaper> = ArrayList()
)
data class Wallpaper(
    @SerializedName("hd_link") val hdLink: String,
    @SerializedName("4k_link") val ultraLink: String
)
