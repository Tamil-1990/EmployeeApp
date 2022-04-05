package com.sample.employeeapp.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sample.employeeapplication.model.AddressConvertor
import com.sample.employeeapplication.model.CompanyConverter
import com.sample.employeeapplication.model.EmployeeDetailsItem


@Database(entities = [EmployeeDetailsItem::class], version = 1, exportSchema = false)
@TypeConverters(AddressConvertor::class, CompanyConverter::class)
abstract class AppDataBase: RoomDatabase() {

    abstract fun getEmployeeDetailsDao(): EmployeeDetailsDAO

    companion object {
        /*Singleton prevents multiple instances of database opening at the same time.*/
        @Volatile
        private var INSTANCE: AppDataBase? = null
        val DB_NAME: String="EmployeeApplication.db"

        fun getDatabase(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, DB_NAME).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}