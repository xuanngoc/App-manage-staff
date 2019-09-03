package com.example.managestaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import user.Teacher;

public class MainActivity extends AppCompatActivity implements TeacherAdapter.OnTeacherListener{

    private static DatabaseReference mFirebaseDatabase;
    private static FirebaseDatabase mFirebaseInstance;


    private FloatingActionButton fabAddTeacher;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private TeacherAdapter mAdapter;
    private List<Teacher> mListTeacher;
    private List<Teacher> mListSearchResult;

    public static int currentTeacherCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAddTeacher = findViewById(R.id.fab_add_teacher);
        recyclerView = findViewById(R.id.list_teachers);
        searchView = findViewById(R.id.searchView);

        recyclerView.setHasFixedSize(true);

        mListTeacher = new ArrayList<>();
        mListSearchResult = new ArrayList<>();

        LinearLayoutManager llm =  new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mAdapter);


        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Teachers");

        getCurrentTeacherCode();


        fabAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddTeacher = new Intent(MainActivity.this, AddTeacherActivity.class );
                startActivity(intentAddTeacher);
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Search View clicked",Toast.LENGTH_LONG).show();
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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mListTeacher.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()){
                    Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                    mListTeacher.add(teacher);
                    //Toast.makeText(MainActivity.this, teacher.getTeachCode(), Toast.LENGTH_LONG).show();
                    //System.out.println(teacher.getTeachCode() + "  " + teacher.fullName);
                }

                mAdapter = new TeacherAdapter( mListTeacher, MainActivity.this);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void addTeacher(String teachCode, String fullName, String birthday,
                                  String gender, String joinDate, float salaryCoefficient){
        Teacher teacher = new Teacher(fullName, birthday, gender, joinDate, salaryCoefficient);

        mFirebaseDatabase.child(teacher.getTeachCode()).setValue(teacher);

    }
    public static void addTeacher(final Teacher teacher, final Context context){



        mFirebaseDatabase.child(teacher.getTeachCode()).setValue(teacher)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Inserting data is success",Toast.LENGTH_LONG).show();
                    }
                });

    }

    public static void updateTeacher(Teacher teacher, final Context context){
        mFirebaseDatabase.child(teacher.getTeachCode()).setValue(teacher)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Updating data is success",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static void deleteTeacher(final Teacher teacher, final Context context){
        mFirebaseDatabase.child(teacher.getTeachCode()).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Deleting data is success" + teacher.getTeachCode(),Toast.LENGTH_LONG).show();
                    }
                });
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
                TeacherAdapter teacherAdapter = new TeacherAdapter( mListSearchResult, MainActivity.this);
                recyclerView.setAdapter(teacherAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                recyclerView.setAdapter(mAdapter);
            }
        });
    }

    private void getCurrentTeacherCode(){
        DatabaseReference databaseReference = mFirebaseInstance.getReference("currentTeacherCode");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                currentTeacherCode = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static void updateCurrentTeacherCode(int currentTeacherCode){
        DatabaseReference databaseReference = mFirebaseInstance.getReference("currentTeacherCode");
        databaseReference.setValue(currentTeacherCode);
    }






    @Override
    public void onTeacherClick(int position) {
        Toast.makeText(this, mListTeacher.get(position).getFullName(), Toast.LENGTH_SHORT).show();
        Teacher teacher = mListTeacher.get(position);
        Intent intent = new Intent(MainActivity.this, DetailATeacher.class);


        intent.putExtra("teacherCode", teacher.getTeachCode());
        intent.putExtra("fullName", teacher.getFullName());
        intent.putExtra("birthday", teacher.getBirthday());
        intent.putExtra("gender", teacher.getGender());
        intent.putExtra("joinDate", teacher.getJoinDate());
        intent.putExtra("salaryCoefficient", teacher.getSalaryCoefficient());

        startActivity(intent);
    }
}
