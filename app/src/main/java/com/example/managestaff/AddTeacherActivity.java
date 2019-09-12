package com.example.managestaff;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import user.Teacher;

public class AddTeacherActivity extends AppCompatActivity {

    private TextInputEditText fullNameView;
    private TextInputEditText birthdayView;
    private TextInputEditText genderView;
    private TextInputEditText joinDateView;
    private TextInputEditText belongDepartmentView;
    private TextInputEditText salaryCoefficientView;



    private MaterialButton btnAddTeacher;
    private MaterialButton btnCancelAddTeacher;


    private static DatabaseReference mFirebaseDatabase;
    private static FirebaseDatabase mFirebaseInstance;

    public static int currentTeacherCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        fullNameView = findViewById(R.id.fullname_edit_text);
        birthdayView = findViewById(R.id.birthday_edit_text);
        genderView  = findViewById(R.id.gender_edit_text);
        joinDateView = findViewById(R.id.join_date_edit_text);
        belongDepartmentView = findViewById(R.id.belong_department_edit_text);
        salaryCoefficientView = findViewById(R.id.salary_coefficient_edit_text);
        btnAddTeacher = findViewById(R.id.btn_add_teacher);
        btnCancelAddTeacher = findViewById(R.id.btn_cancel_add_teacher);


        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Departments");



        final String departmentCode = getIntent().getExtras().get("departmentCode").toString();
        getCurrentTeacherCode(departmentCode);

        birthdayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog

                DatePickerDialog datePicker = new DatePickerDialog(AddTeacherActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthdayView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        joinDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog

                DatePickerDialog datePicker = new DatePickerDialog(AddTeacherActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                joinDateView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });


        belongDepartmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final AlertDialog.Builder builder = new AlertDialog.Builder(AddTeacherActivity.this);
               View view = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
               builder.setTitle("Chọn bo môn");
                final Spinner spinner = view.findViewById(R.id.spinner);


                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTeacherActivity.this, android.R.layout.simple_spinner_item,
                        MainActivity.listDepartmentName  );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        belongDepartmentView.setText(spinner.getSelectedItem().toString());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        btnAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departmentName =  belongDepartmentView.getText().toString();
                String departmentCode = MainActivity.getDepartmentCode(departmentName);

                /*System.out.println("Start to get data");
                getCurrentTeacherCode(departmentCode);

                System.out.println("fuck this shit ");

                System.out.println("current123 = " + currentTeacherCode);*/
                Teacher teacher = new Teacher(departmentCode,fullNameView.getText().toString(),
                        birthdayView.getText().toString(),
                        genderView.getText().toString(),
                        joinDateView.getText().toString(),
                        Float.parseFloat( salaryCoefficientView.getText().toString()) );




                addTeacher(departmentCode, teacher,getApplicationContext());

                System.out.println("Come on, pls");
                //Toast.makeText(AddTeacherActivity.this, "Clicked", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btnCancelAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    public static void addTeacher(String departmentCode, final Teacher teacher, final Context context){
        mFirebaseDatabase.child(departmentCode).child("Teachers").child(teacher.getTeachCode()).setValue(teacher)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Inserting data is success",Toast.LENGTH_LONG).show();
                    }
                });

        updateCurrentTeacherCode(departmentCode);
    }
    /*public void addTeacher(String departmentCode , String fullName, String birthday, String gender,
                                  String joinDate, float salaryCoefficient, final Context context){

        Teacher teacher = new Teacher(departmentCode, fullName, birthday, gender, joinDate, salaryCoefficient);
        System.out.println("pls111");
        getCurrentTeacherCode(departmentCode);
        System.out.println("pls222");


        mFirebaseDatabase.child(departmentCode).child("Teachers").child(teacher.getTeachCode()).setValue(teacher)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Inserting data is success",Toast.LENGTH_LONG).show();
                    }
                });

        updateCurrentTeacherCode(departmentCode);
    }*/

    private void getCurrentTeacherCode(String departmentCode){
        DatabaseReference databaseReference = mFirebaseInstance.getReference("Departments").child(departmentCode).child("currentTeacherCode");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("gggg  " + dataSnapshot.getValue());

                currentTeacherCode = Integer.parseInt(dataSnapshot.getValue().toString());

                System.out.println("beee  " + currentTeacherCode);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static void updateCurrentTeacherCode(String departmentCode){
        DatabaseReference databaseReference = mFirebaseInstance.getReference("Departments").child(departmentCode).child("currentTeacherCode");
        databaseReference.setValue(++ currentTeacherCode);
    }

}
