package com.sample.employeeapp.employeelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.sample.employeeapp.dao.AppDataBase
import com.sample.employeeapp.repository.*
import kotlinx.coroutines.Dispatchers

class EmployeeListViewModel(var mainRepository: MainRepository, application: Application): AndroidViewModel(application) {

    companion object{
        val TAG: String =  EmployeeListViewModel::class.java.name
    }

    var appDataBase: AppDataBase?=null
    var apiInterface: APIInterface?=null
    var applications: Application? = null


    init {
        appDataBase = AppDataBase.getDatabase(application)
        apiInterface = APIClient.getClient()
        applications = application
    }

    fun getServerEmployeesList() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            var responseFromDB = appDataBase?.getEmployeeDetailsDao()?.getAllEmployeeDetailsList()
            if(responseFromDB != null && responseFromDB.isNotEmpty()) {
                emit(Resource.success(data = responseFromDB))
            }else {
                if (Util.isNetworkAvailable(applications!!)) {
                    val apiResponse = mainRepository.getEmployeeDetails()
                    appDataBase!!.getEmployeeDetailsDao().insertEmployeeDetails(apiResponse)
                    responseFromDB = appDataBase?.getEmployeeDetailsDao()?.getAllEmployeeDetailsList()
                    if(responseFromDB != null && responseFromDB.isNotEmpty()) {
                        emit(Resource.success(data = responseFromDB))
                    }else {
                        emit(Resource.error(data = null, message = "No records found"))
                    }
                }else {
                    emit(Resource.error(data = null, message = "No network, check your network connection"))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}