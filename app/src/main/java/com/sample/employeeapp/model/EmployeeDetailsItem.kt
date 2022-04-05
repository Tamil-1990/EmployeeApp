package com.sample.employeeapplication.model

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Entity(tableName = "EmployeeDetailsItem")
data class EmployeeDetailsItem(
    @TypeConverters(AddressConvertor::class)
    val address: Address?,
    @TypeConverters(CompanyConverter::class)
    val company: Company?,
    val email: String?,
    @PrimaryKey
    val id: Int,
    val name: String?,
    val phone: String?,
    val profile_image: String?,
    val username: String?,
    val website: String?
): Parcelable