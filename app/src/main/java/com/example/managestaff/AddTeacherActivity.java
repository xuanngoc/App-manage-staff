package com.example.managestaff;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import user.Teacher;

public class AddTeacherActivity extends AppCompatActivity {

    private TextInputEditText fullNameView;
    private TextInputEditText birthdayView;
    private TextInputEditText genderView;
    private TextInputEditText joinDateView;
    private TextInputEditText salaryCoefficientView;



    private MaterialButton btnAddTeacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        fullNameView = findViewById(R.id.fullname_edit_text);
        birthdayView = findViewById(R.id.birthday_edit_text);
        genderView  = findViewById(R.id.gender_edit_text);
        joinDateView = findViewById(R.id.join_date_edit_text);
        salaryCoefficientView = findViewById(R.id.salary_coefficient_edit_text);
        btnAddTeacher = findViewById(R.id.btn_add_teacher);


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



        btnAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher teacher = new Teacher(fullNameView.getText().toString(),
                        birthdayView.getText().toString(),
                        genderView.getText().toString(),
                        joinDateView.getText().toString(),
                        Float.parseFloat( salaryCoefficientView.getText().toString()) );
                MainActivity.addTeacher(teacher, getApplicationContext());
                Toast.makeText(AddTeacherActivity.this, "Clicked", Toast.LENGTH_LONG).show();
                finish();
            }
        });




    }
}
