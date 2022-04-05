package com.sample.employeeapplication.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AddressConvertor {
    @TypeConverter
    fun fromAddress(value:Address?): String{
        val gson = Gson()
        val type = object: TypeToken<Address>(){}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAddress(value:String): Address?{
        val gson = Gson()
        val type = object: TypeToken<Address>(){}.type
        return gson.fromJson(value, type)
    }
}
