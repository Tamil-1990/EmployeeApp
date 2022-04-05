package com.sample.employeeapp.employeelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.employeeapp.R
import com.sample.employeeapplication.model.EmployeeDetailsItem


class EmployeeAdapter(val context: Context, val employeeDetailsList: List<EmployeeDetailsItem>, val listener: Listener): RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    interface Listener {
        fun onItemClick(position: Int, viewName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employee_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employeeDetails: EmployeeDetailsItem = employeeDetailsList[position]

        if(employeeDetails.profile_image != null) {
            val profileImgUrl: String = employeeDetails.profile_image
            if (profileImgUrl.isNotEmpty()) {
                Glide.with(context)
                    .load(profileImgUrl)
                    .into(holder.empImgView)
            }
        }

        holder.txtEmpName.text = employeeDetails.name

        if(employeeDetails.company != null) {
            holder.txtCompanyName.text = employeeDetails.company.name
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(position, "SELECTED_ITEM")
        }
    }

    override fun getItemCount(): Int {
        return employeeDetailsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val empImgView: ImageView
        val txtEmpName: TextView
        val txtCompanyName: TextView

        init {
            empImgView = itemView.findViewById(R.id.emp_image_view) as ImageView
            txtEmpName = itemView.findViewById(R.id.txt_employee_name) as TextView
            txtCompanyName = itemView.findViewById(R.id.txt_company_name) as TextView
        }
    }
}