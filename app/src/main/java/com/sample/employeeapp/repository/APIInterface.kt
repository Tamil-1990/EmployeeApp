package com.sample.employeeapp.repository

import com.sample.employeeapplication.model.EmployeeDetailsItem
import retrofit2.http.GET

interface APIInterface {

    @GET("v2/5d565297300000680030a986")
    suspend fun getEmployeeDetails() : List<EmployeeDetailsItem>
}