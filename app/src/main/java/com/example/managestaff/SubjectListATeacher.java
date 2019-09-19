package com.example.managestaff;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import user.Department;
import user.Subject;
import user.Teacher;

public class SubjectListATeacher extends AppCompatActivity {


    private ListView mList;
    private FloatingActionButton fabAddSubject;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private static SubjectAdapter mAdapter;
    private List<Subject> mListSubject1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list_ateacher);

        //get course catalog
        ShowListSubjectActivity.getSubjectName();

        mList = findViewById(R.id.listView);
        fabAddSubject = findViewById(R.id.fab_add_subject);
        fabAddSubject.setOnClickListener(fabAddTeacherClickListener);

        Bundle b = getIntent().getExtras();
        String departmentCode = b.get("departmentCode").toString();
        String teacherCode = b.get("teacherCode").toString();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Departments").child(departmentCode).child("Teachers").child(teacherCode);

        mListSubject1 = new ArrayList<>();
        mAdapter = new SubjectAdapter(this, mListSubject1);
        mList.setAdapter(mAdapter);


    }

    View.OnClickListener fabAddTeacherClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mListSubject1.size() < 3){
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SubjectListATeacher.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_spinner_add_subject, null);
                builder.setTitle("Chọn bo môn");
                final Spinner spinner = view.findViewById(R.id.spinner);


                ArrayAdapter<String> adapter = new ArrayAdapter<>(SubjectListATeacher.this, android.R.layout.simple_spinner_item,
                        ShowListSubjectActivity.mListSubjectName  );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //belongDepartmentView.setText(spinner.getSelectedItem().toString());
                        //mListSubject1.add(ShowListSubjectActivity.getSubject(spinner.getSelectedItem().toString()));
                        addSubject(ShowListSubjectActivity.getSubject(spinner.getSelectedItem().toString()));
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
            }else{
                Toast.makeText(SubjectListATeacher.this, "Đã đến số môn tối đa được dạy", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseDatabase.child("subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mListSubject1.clear();

                for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){
                    Subject subject = subjectSnapshot.getValue(Subject.class);
                    System.out.println(subject.getSubjectName());
                    mListSubject1.add(subject);
                    //Toast.makeText(MainActivity.this, teacher.getTeachCode(), Toast.LENGTH_LONG).show();
                }

                mAdapter = new SubjectAdapter( SubjectListATeacher.this, mListSubject1);
                mList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_a_teacher, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_edit:
                Bundle b = getIntent().getExtras();

                Intent i = new Intent(SubjectListATeacher.this, DetailATeacher.class);

                i.putExtra("departmentCode", b.get("departmentCode").toString());
                i.putExtra("teacherCode", b.get("teacherCode").toString());
                i.putExtra("teacherName", b.get("teacherName").toString());
                i.putExtra("teacherBirthday", b.get("teacherBirthday").toString());
                i.putExtra("teacherGender", b.get("teacherGender").toString());
                i.putExtra("teacherJoinDate", b.get("teacherJoinDate").toString());
                i.putExtra("teacherSalaryCoefficient", b.get("teacherSalaryCoefficient").toString());
                System.out.println(b.get("teacherName").toString());
                startActivity(i);
                break;

        }


        return super.onOptionsItemSelected(item);
    }
    private void addSubject(Subject subject){
        mFirebaseDatabase.child("subjects").child(subject.getSubjectCode()).setValue(subject);
    }
}
