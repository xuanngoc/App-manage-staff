package com.example.managestaff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import user.Department;

public class DepartmentAdapter extends ArrayAdapter<Department>  {
    private List<Department> list;
    Context context;

    public DepartmentAdapter(Context context, List<Department> list){
        super(context, R.layout.list_department_item, list);
        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View listItemView = layoutInflater.inflate(R.layout.list_department_item, null, false);


        TextView departmentCodeView = listItemView.findViewById(R.id.department_code_view);
        TextView departmentNameView = listItemView.findViewById(R.id.department_name_view);

        Department currentDepartment = list.get(position);

        departmentCodeView.setText(currentDepartment.getDepartmentCode());
        departmentNameView.setText(currentDepartment.getDepartmentName());
        System.out.println( "GGG: " + currentDepartment.getDepartmentName());


        return listItemView;
    }
}
