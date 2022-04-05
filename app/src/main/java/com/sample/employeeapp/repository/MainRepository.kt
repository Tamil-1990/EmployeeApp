package com.sample.employeeapp.repository

class MainRepository(private val apiHelper: APIInterface) {

    suspend fun getEmployeeDetails() = apiHelper.getEmployeeDetails()
}