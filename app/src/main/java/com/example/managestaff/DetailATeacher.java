package com.example.managestaff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import user.Teacher;

public class DetailATeacher extends AppCompatActivity {

    private TextInputEditText teacherCodeView;
    private TextInputEditText fullNameView;
    private TextInputEditText genderView;
    private TextInputEditText birthdayView;
    private TextInputEditText joinDateView;
    private TextInputEditText salaryCoefficientView;


    private MaterialButton btnChangeInfo;
    private MaterialButton btnDeleteTeacher;
    private MaterialButton btnSaveInfo;

    private static DatabaseReference mFirebaseDatabase;
    private static FirebaseDatabase mFirebaseInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ateacher);

        teacherCodeView = findViewById(R.id.teacher_code_edit_text);
        fullNameView = findViewById(R.id.fullname_edit_text);
        genderView = findViewById(R.id.gender_edit_text);
        birthdayView = findViewById(R.id.birthday_edit_text);
        joinDateView = findViewById(R.id.join_date_edit_text);
        salaryCoefficientView = findViewById(R.id.salary_coefficient_edit_text);

        btnChangeInfo = findViewById(R.id.btn_change_info);
        btnDeleteTeacher = findViewById(R.id.btn_del_teacher);
        btnSaveInfo = findViewById(R.id.btn_save_info);






        Bundle bundle = getIntent().getExtras();
        teacherCodeView.setText(bundle.get("teacherCode").toString());
        fullNameView.setText(bundle.get("teacherName").toString());
        birthdayView.setText(bundle.get("teacherBirthday").toString());
        genderView.setText(bundle.get("teacherGender").toString());
        joinDateView.setText(bundle.get("teacherJoinDate").toString());
        salaryCoefficientView.setText(bundle.get("teacherSalaryCoefficient").toString());

        final String departmentCode = bundle.get("departmentCode").toString();

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Departments").child(departmentCode).child("Teachers");


        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //teacherCodeView.setEnabled(true); teacherCode can't be change
                fullNameView.setEnabled(true);
                birthdayView.setEnabled(true);
                genderView.setEnabled(true);
                joinDateView.setEnabled(true);
                salaryCoefficientView.setEnabled(true);
                btnSaveInfo.setVisibility(View.VISIBLE);

                /// set dialog pick date
                birthdayView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog

                        DatePickerDialog datePicker = new DatePickerDialog(DetailATeacher.this,
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

                        DatePickerDialog datePicker = new DatePickerDialog(DetailATeacher.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        joinDateView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        datePicker.show();
                    }
                });
            }
        });

        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher teacher = new Teacher(teacherCodeView.getText().toString(),
                        fullNameView.getText().toString(),
                        birthdayView.getText().toString(),
                        genderView.getText().toString(),
                        joinDateView.getText().toString(),
                        Float.parseFloat(salaryCoefficientView.getText().toString()));

                System.out.println("FF" + departmentCode);
                updateTeacher(teacher);
                finish();
            }
        });

        btnDeleteTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        DetailATeacher.this);

                // set title
                alertDialogBuilder.setTitle("Xóa giáo viên");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Bạn vẫn muốn xóa giáo viên này ?")
                        .setCancelable(false)
                        .setPositiveButton("Có",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                Teacher teacher = new Teacher(teacherCodeView.getText().toString(),
                                        fullNameView.getText().toString(),
                                        birthdayView.getText().toString(),
                                        genderView.getText().toString(),
                                        joinDateView.getText().toString(),
                                        Float.parseFloat(salaryCoefficientView.getText().toString()));

                                deleteTeacher(teacher);


                                // if this button is clicked, close
                                finish();

                            }
                        })
                        .setNegativeButton("Không",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

    }

    public void updateTeacher(final Teacher teacher){
        mFirebaseDatabase.child(teacher.getTeachCode()).setValue(teacher)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailATeacher.this, "Updating data is success",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void deleteTeacher(final Teacher teacher){
        mFirebaseDatabase.child(teacher.getTeachCode()).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailATeacher.this, "Deleting data is success" + teacher.getTeachCode(),Toast.LENGTH_LONG).show();
                    }
                });
    }


}
