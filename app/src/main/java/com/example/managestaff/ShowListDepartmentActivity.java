package com.example.managestaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import user.Department;

public class ShowListDepartmentActivity extends AppCompatActivity{

    private static DatabaseReference mFirebaseDatabase;
    private static FirebaseDatabase mFirebaseInstance;


    //    private Button btnAddTeacher;
    private FloatingActionButton btnAddDepartment;
    private ListView listDepartmentView;



    public static List<String> listDepartmentName;
    public static List<Department> listDepartment;
    private DepartmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_department);

        btnAddDepartment = findViewById(R.id.fab_add_department);
        listDepartmentView = findViewById(R.id.list_department_view);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Departments");

        listDepartmentName = new LinkedList<>();
        listDepartment = new ArrayList<>();
        getListDepartmentName();
        getListDepartment();

        mAdapter = new DepartmentAdapter(this, listDepartment);
        listDepartmentView.setAdapter(mAdapter);

        btnAddDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialogBuilder = new AlertDialog.Builder(ShowListDepartmentActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog_add_department, null);

                final TextInputEditText departmentCodeEditText =  dialogView.findViewById(R.id.department_code_edit_text);
                final TextInputEditText departmentNameEditText = dialogView.findViewById(R.id.department_name_edit_text);
                MaterialButton btnAdd = dialogView.findViewById(R.id.btn_add_a_department);
                MaterialButton btnCancel = dialogView.findViewById(R.id.btn_cancel_add_department);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Department department = new Department(departmentCodeEditText.getText().toString(),
                                departmentNameEditText.getText().toString());
                        addDepartment(department);
                        dialogBuilder.dismiss();
                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();

            }
        });

        listDepartmentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Department department = (Department) parent.getItemAtPosition(position);
                Toast.makeText(ShowListDepartmentActivity.this,"-----------" + department.getDepartmentCode(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShowListDepartmentActivity.this,  ShowListTeacherActivity.class);
                intent.putExtra("departmentCode", department.getDepartmentCode());
                intent.putExtra("departmentName", department.getDepartmentName());
                startActivity(intent);
            }
        });

    }

    public void addDepartment(Department department){
        mFirebaseDatabase.child(department.getDepartmentCode()).setValue(department)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ShowListDepartmentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });
        mFirebaseDatabase.child(department.getDepartmentCode()).child("currentTeacherCode").setValue(0);
    }



    public void getListDepartmentName(){
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listDepartmentName.clear();
                for(DataSnapshot departmentSnapshot : dataSnapshot.getChildren()){
                    Department department = departmentSnapshot.getValue(Department.class);
                    listDepartmentName.add(department.getDepartmentName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getListDepartment(){
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listDepartment.clear();
                for(DataSnapshot departmentSnapshot : dataSnapshot.getChildren()){
                    Department department  = departmentSnapshot.getValue(Department.class);

                    listDepartment.add(department);
                    System.out.println("GG " + department.getDepartmentName());
                }
                mAdapter = new DepartmentAdapter(ShowListDepartmentActivity.this, listDepartment);
                listDepartmentView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static String getDepartmentCode(String departmentName){
        for(int i = 0; i < listDepartment.size(); i++ ){
            if(listDepartment.get(i).getDepartmentName().equals(departmentName )){
                return listDepartment.get(i).getDepartmentCode();
            }
        }
        return null;
    }
}
