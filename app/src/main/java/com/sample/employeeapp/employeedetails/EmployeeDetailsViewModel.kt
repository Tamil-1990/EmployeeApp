package com.sample.employeeapp.employeedetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class EmployeeDetailsViewModel(application: Application) : AndroidViewModel(application)  {

    companion object{
        val TAG: String =  EmployeeDetailsViewModel::class.java.name
    }
}