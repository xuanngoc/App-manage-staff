package com.example.managestaff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

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


        /*intent.putExtra("teacherCode", teacher.getTeachCode());
        intent.putExtra("fullName", teacher.getFullName());
        intent.putExtra("birthday", teacher.getBirthday());
        intent.putExtra("gender", teacher.getGender());
        intent.putExtra("joinDate", teacher.getJoinDate());
        intent.putExtra("salaryCoefficient", teacher.getSalaryCoefficient());*/

        Bundle bundle = getIntent().getExtras();
        teacherCodeView.setText(bundle.get("teacherCode").toString());
        fullNameView.setText(bundle.get("fullName").toString());
        birthdayView.setText(bundle.get("birthday").toString());
        genderView.setText(bundle.get("gender").toString());
        joinDateView.setText(bundle.get("joinDate").toString());
        salaryCoefficientView.setText(bundle.get("salaryCoefficient").toString());


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
            }
        });

        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher teacher = new Teacher(fullNameView.getText().toString(),
                        birthdayView.getText().toString(),
                        genderView.getText().toString(),
                        joinDateView.getText().toString(),
                        Float.parseFloat(salaryCoefficientView.getText().toString()));

                MainActivity.updateTeacher(teacher, getApplicationContext());
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

                                MainActivity.deleteTeacher(teacher, getApplicationContext());


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
}
