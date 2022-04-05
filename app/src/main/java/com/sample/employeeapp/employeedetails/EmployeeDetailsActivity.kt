package com.sample.employeeapp.employeedetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sample.employeeapp.R
import com.sample.employeeapp.databinding.EmployeeDetailsBinding
import com.sample.employeeapplication.model.EmployeeDetailsItem


class EmployeeDetailsActivity: AppCompatActivity() {

    companion object{
        val TAG: String = EmployeeDetailsActivity::class.java.name
        var employeeDetails: EmployeeDetailsItem? = null
    }

    var binding: EmployeeDetailsBinding? = null
    var employeeDetailsViewModel: EmployeeDetailsViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.employee_details)
        employeeDetailsViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            EmployeeDetailsViewModel::class.java)

        binding?.employeeDetailsViewModel = employeeDetailsViewModel
        binding?.employeeDetailsView = this

        if(intent.extras != null){
            employeeDetails = intent.getParcelableExtra(resources.getString(R.string.employee_details))!!
            displayEmployeeInfo()
        }
    }

    private fun displayEmployeeInfo() {
        if(employeeDetails!!.profile_image != null) {
            val profileImgUrl: String = employeeDetails!!.profile_image!!
            if (profileImgUrl.isNotEmpty()) {
                Glide.with(this)
                    .load(profileImgUrl)
                    .into(binding?.empImageView!!)
            }
        }

        val name: String = "Employee Name: "+employeeDetails!!.name
        val userName: String = "Emp User Name: "+employeeDetails!!.username
        val email: String = "Email Id: "+employeeDetails!!.email
        val addressDetails: String = "Address: "+employeeDetails!!.address?.street+", "+ employeeDetails!!.address?.suite+", "+
                employeeDetails!!.address?.city+", "+ employeeDetails!!.address?.zipcode
        val phoneNo: String = "Phone No: "+ employeeDetails!!.phone

        binding?.txtEmployeeName?.text = name
        binding?.txtEmployeeUserName?.text = userName
        binding?.txtEmailAddress?.text = email
        binding?.txtAddress?.text = addressDetails
        binding?.txtPhoneNo?.text = phoneNo

        if(employeeDetails!!.website != null) {
            val website: String = "WebSite: " + employeeDetails!!.website
            binding?.txtWebsite?.text = website
        }
        if(employeeDetails!!.company != null) {
            val companyDetails: String =
                "Company Name: " + employeeDetails!!.company?.name + ", \nCatchPhrase: " +
                        employeeDetails!!.company?.catchPhrase + ", \nbs:" + employeeDetails!!.company?.bs
            binding?.txtCompanyDetails?.text = companyDetails
        }
    }
}