package com.sample.employeeapplication.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CompanyConverter {
    @TypeConverter
    fun fromCompany(value:Company?): String{
        val gson = Gson()
        val type = object: TypeToken<Company>(){}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCompany(value:String): Company?{
        val gson = Gson()
        val type = object: TypeToken<Company>(){}.type
        return gson.fromJson(value, type)
    }
}