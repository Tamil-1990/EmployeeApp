package com.sample.employeeapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.employeeapplication.model.EmployeeDetailsItem

@Dao
interface EmployeeDetailsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmployeeDetails(employeeDetails: List<EmployeeDetailsItem>)

    @Query("Select *from employeedetailsitem")
    fun getAllEmployeeDetailsList(): List<EmployeeDetailsItem>

    @Query("Select *from employeedetailsitem where id=:employeeId")
    fun getOneEmployeeDetails(employeeId: Int): LiveData<EmployeeDetailsItem>
}