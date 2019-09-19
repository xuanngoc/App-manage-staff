package com.example.managestaff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
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
import user.Teacher;

public class ShowListTeacherActivity extends AppCompatActivity {

    private static DatabaseReference mFirebaseDatabase;
    private static FirebaseDatabase mFirebaseInstance;


    private TextView departmentNameView;
    private FloatingActionButton fabAddTeacher;
    private ListView listView;
    private SearchView searchView;
    private TextView emptyView;

    private TeacherAdapter mAdapter;
    private List<Teacher> mListTeacher;
    private List<Teacher> mListSearchResult;

//    public static int currentTeacherCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_teacher);

        departmentNameView = findViewById(R.id.department_name_view);
        fabAddTeacher = findViewById(R.id.fab_add_teacher);
        listView = findViewById(R.id.list_teacher);
        searchView = findViewById(R.id.searchView);
        emptyView = findViewById(R.id.emptyView);
        listView.setEmptyView(emptyView);

        Bundle bundle = getIntent().getExtras();
        final String departmentCode = bundle.get("departmentCode").toString();
        final String departmentName = bundle.get("departmentName").toString();

        departmentNameView.setText(departmentName);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Departments").child(departmentCode).child("Teachers");

        mListTeacher = new ArrayList<>();
        mListSearchResult = new ArrayList<>();

        mAdapter = new TeacherAdapter(this, mListTeacher);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Teacher teacher = (Teacher) parent.getItemAtPosition(position);
                Toast.makeText(ShowListTeacherActivity.this, teacher.getFullName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ShowListTeacherActivity.this, SubjectListATeacher.class);

                intent.putExtra("departmentCode", getIntent().getExtras().get("departmentCode").toString());
                intent.putExtra("teacherCode", teacher.getTeachCode());
                intent.putExtra("teacherName", teacher.getFullName());
                intent.putExtra("teacherBirthday", teacher.getBirthday());
                intent.putExtra("teacherGender", teacher.getGender());
                intent.putExtra("teacherJoinDate", teacher.getJoinDate());
                intent.putExtra("teacherSalaryCoefficient", teacher.getSalaryCoefficient());

                startActivity(intent);
            }
        });



        fabAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddTeacher = new Intent(ShowListTeacherActivity.this, AddTeacherActivity.class );
                intentAddTeacher.putExtra("departmentCode", getIntent().getExtras().get("departmentCode").toString());
                startActivity(intentAddTeacher);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTeacher(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTeacher(newText);
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mListTeacher.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()){
                    Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                    mListTeacher.add(teacher);
                    //Toast.makeText(MainActivity.this, teacher.getTeachCode(), Toast.LENGTH_LONG).show();
                    //System.out.println(teacher.getTeachCode() + "  " + teacher.fullName);
                }

                mAdapter = new TeacherAdapter( ShowListTeacherActivity.this, mListTeacher);
                listView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_edit:
                Toast.makeText(ShowListTeacherActivity.this, "edit button", Toast.LENGTH_SHORT).show();
                final AlertDialog dialogBuilder = new AlertDialog.Builder(ShowListTeacherActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog_add_department, null);

                final TextInputEditText departmentCodeEditText =  dialogView.findViewById(R.id.department_code_edit_text);
                final TextInputEditText departmentNameEditText = dialogView.findViewById(R.id.department_name_edit_text);

                departmentCodeEditText.setText(getIntent().getExtras().get("departmentCode").toString());
                departmentNameEditText.setText(getIntent().getExtras().get("departmentName").toString());

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

                        editInfoDepartment(department);
                        dialogBuilder.dismiss();
                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
                break;

            case R.id.action_delete:
                Toast.makeText(ShowListTeacherActivity.this, "delete button", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ShowListTeacherActivity.this);

                // set title
                alertDialogBuilder.setTitle("Xóa bộ môn");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Bạn vẫn muốn xóa bộ môn này ?")
                        .setCancelable(false)
                        .setPositiveButton("Có",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                               deleteDepartment();

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
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void searchTeacher(final String value){
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListSearchResult.clear();
                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()){
                    Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                    if( teacher.getTeachCode().equals(value) || teacher.getFullName().contains(value)){
                        mListSearchResult.add(teacher);
                        System.out.println(teacher.getFullName());
                    }

                    ///
                    try{
                        int ageQuery = Integer.parseInt(value);
                        if(teacher.getAge() >= ageQuery){
                            mListSearchResult.add(teacher);
                            System.out.println(teacher.getFullName() + "  age = " + teacher.getAge());
                        }
                    }catch(NumberFormatException ex){ // handle your exception

                    }
                }
                TeacherAdapter teacherAdapter = new TeacherAdapter( ShowListTeacherActivity.this, mListSearchResult);
                listView.setAdapter(teacherAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listView.setAdapter(mAdapter);
            }
        });
    }


    private void editInfoDepartment(Department department){
        String departmentCode = getIntent().getExtras().get("departmentCode").toString();
        DatabaseReference databaseReference = mFirebaseDatabase = mFirebaseInstance.getReference("Departments").child(departmentCode);
        databaseReference.setValue(department);

    }
    private void deleteDepartment(){
        String departmentCode = getIntent().getExtras().get("departmentCode").toString();
        DatabaseReference databaseReference = mFirebaseDatabase = mFirebaseInstance.getReference("Departments").child(departmentCode);
        databaseReference.setValue(null);

    }





}
