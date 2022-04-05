package com.sample.employeeapp.employeelist

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.employeeapp.R
import com.sample.employeeapp.databinding.EmployeeListBinding
import com.sample.employeeapp.employeedetails.EmployeeDetailsActivity
import com.sample.employeeapp.repository.APIClient
import com.sample.employeeapp.repository.Status
import com.sample.employeeapp.repository.ViewModelFactory
import com.sample.employeeapplication.model.EmployeeDetailsItem


class EmployeeListActivity: AppCompatActivity(), EmployeeAdapter.Listener {
    companion object{
        val TAG: String = EmployeeListActivity::class.java.name
        var alertDialog: AlertDialog? = null
    }

    var binding: EmployeeListBinding? = null
    var employeeListViewModel: EmployeeListViewModel? = null

    val RUN_TIME_PERMISSION_REQUEST_CODE:Int = 9001

    val permissions = arrayOf<String>(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)

    var employeeDetailsList: List<EmployeeDetailsItem>? = null
    var employeeAdapter: EmployeeAdapter? = null
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.employee_list)
        supportActionBar!!.title = "Employees List"

        binding?.employeeListViewModel = employeeListViewModel
        binding?.employeeListView = this
        employeeListViewModel = ViewModelProvider(this, ViewModelFactory(APIClient.getClient(),application))[EmployeeListViewModel::class.java]

        binding?.txtNoData?.setOnClickListener {
            startLayoutVisible()
        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= 23) {/* for Marshmallow+*/
            if (checkAndRequestPermissions()) {
                startLayoutVisible()
            }
        } else {
            startLayoutVisible()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkAndRequestPermissions(): Boolean {
        val listPermissionsNeeded = ArrayList<String>()
        for (permission in permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission)
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                listPermissionsNeeded.toTypedArray(), RUN_TIME_PERMISSION_REQUEST_CODE)
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RUN_TIME_PERMISSION_REQUEST_CODE) {
            val permissionResult: HashMap<String, Int> = HashMap()
            var deniedCount: Int = 0

            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResult.put(permissions[i], grantResults[i])
                    deniedCount++
                }
            }

            if (deniedCount == 0) {
                startLayoutVisible()
            } else {
                for ((permName, permResult) in permissionResult) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        permissionDialog(resources.getString(R.string.app_permissions),
                            resources.getString(R.string.app_required_permission),
                            resources.getString(R.string.yes),
                            resources.getString(R.string.no_exit_app))
                        break
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun permissionDialog(
        title: String,
        message: String,
        positiveTxt: String,
        negativeTxt: String) {

        if(alertDialog != null && alertDialog!!.isShowing){
            alertDialog!!.dismiss()
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        /*alert dialog positive button*/
        builder.setPositiveButton(positiveTxt) { dialog, which ->
            if (positiveTxt.equals(resources.getString(R.string.yes))) {
                dialog.dismiss()
                checkAndRequestPermissions()
            } else if (positiveTxt.equals(resources.getString(R.string.go_settings))) {
                dialog.dismiss()
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }

        /*alert dialog negative button*/
        builder.setNegativeButton(negativeTxt) { dialog, which ->
            dialog.dismiss()
            finish()
        }

        builder.setCancelable(false)

        if(!isFinishing) {
            alertDialog = builder.create()
            alertDialog!!.setCanceledOnTouchOutside(false)
            alertDialog!!.show()
        }
    }

    private fun startLayoutVisible() {
        binding?.employeeRecyclerView?.visibility = View.GONE
        binding?.progressBar?.visibility = View.VISIBLE

        callAPI()
    }

    private fun callAPI() {
        employeeListViewModel?.getServerEmployeesList()?.observe(this,{
            it?.let {
                Log.i(TAG, "callAPI test"+ it.status)
                when(it.status){
                    Status.LOADING ->{
                        binding?.progressBar?.visibility = View.VISIBLE
                    }

                    Status.SUCCESS ->{
                        binding?.progressBar?.visibility = View.GONE
                        employeeDetailsList = it.data
                        loadAdapter()
                    }

                    Status.ERROR ->{
                        binding?.progressBar?.visibility = View.GONE
                        binding?.txtNoData?.visibility = View.VISIBLE
                        binding?.txtNoData?.text = it.message
                    }
                }
            }
        })
    }

    private fun loadAdapter() {
        Log.i(TAG, "Adapter test")
        binding?.txtNoData?.visibility = View.GONE
        binding?.employeeRecyclerView?.visibility = View.VISIBLE

        linearLayoutManager = LinearLayoutManager(this)
        binding?.employeeRecyclerView?.layoutManager = linearLayoutManager
        employeeAdapter = EmployeeAdapter(this, employeeDetailsList!!, this)
        binding?.employeeRecyclerView?.adapter = employeeAdapter
    }

    override fun onItemClick(position: Int, viewName: String) {
        val employeeDetails: EmployeeDetailsItem = employeeDetailsList!![position]
        val intent = Intent(this, EmployeeDetailsActivity::class.java)
        intent.putExtra(resources.getString(R.string.employee_details), employeeDetails)
        startActivity(intent)
    }
}