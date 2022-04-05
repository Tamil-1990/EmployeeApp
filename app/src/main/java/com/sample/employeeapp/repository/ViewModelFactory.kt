package com.sample.employeeapp.repository

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.employeeapp.employeelist.EmployeeListViewModel

class ViewModelFactory (private val apiHelper: APIInterface,var application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmployeeListViewModel::class.java)) {
            return EmployeeListViewModel(MainRepository(apiHelper),application = application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}