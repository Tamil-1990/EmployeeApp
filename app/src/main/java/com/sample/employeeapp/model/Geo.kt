package com.sample.employeeapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Geo(
    val lat: String?,
    val lng: String?
): Parcelable