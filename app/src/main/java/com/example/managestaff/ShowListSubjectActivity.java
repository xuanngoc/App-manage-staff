package com.example.managestaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
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
import user.Subject;
import user.Teacher;

public class ShowListSubjectActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private ListView mList;
    private FloatingActionButton mfabAddSubject;

    private static DatabaseReference mFirebaseDatabase;
    private static FirebaseDatabase mFirebaseInstance;

    public static List<Subject> mListSubject;
    public static List<String> mListSubjectName;

    private SubjectAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_subject);


        mSearchView = findViewById(R.id.search_view);
        mList = findViewById(R.id.list_subject_view);
        mfabAddSubject = findViewById(R.id.fab_add_subject);

        mSearchView.setOnQueryTextListener(searchViewQueryListener);
        mSearchView.setOnCloseListener(closeListener);
        mfabAddSubject.setOnClickListener(fabAddSubjectClick);


        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Subjects");

        mListSubject = new ArrayList<>();
        mListSubjectName = new LinkedList<>();
        mAdapter = new SubjectAdapter(this, mListSubject);
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(itemClickListener);
        getSubjectName();

    }

    View.OnClickListener fabAddSubjectClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog dialogBuilder = new AlertDialog.Builder(ShowListSubjectActivity.this).create();
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog_add_subject, null);

            final TextInputEditText subjectCodeEditText =  dialogView.findViewById(R.id.subject_code_edit_text);
            final TextInputEditText subjectNameEditText = dialogView.findViewById(R.id.subject_name_edit_text);
            final TextInputEditText subjectCreditsEditText = dialogView.findViewById(R.id.subject_credits_edit_text);
            final TextInputEditText subjectHoursEditText = dialogView.findViewById(R.id.subject_hours_edit_text);
            final TextInputEditText subjectCoefficientEditText = dialogView.findViewById(R.id.subject_coefficient_edit_text);
            MaterialButton btnAdd = dialogView.findViewById(R.id.btn_add_subject);
            MaterialButton btnCancel = dialogView.findViewById(R.id.btn_cancel_add_suject);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder.dismiss();
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Subject subject = new Subject(subjectCodeEditText.getText().toString(),
                            subjectNameEditText.getText().toString(),
                            subjectCreditsEditText.getText().toString(),
                            subjectHoursEditText.getText().toString(),
                            subjectCoefficientEditText.getText().toString());

                    addSubject(subject);

                    dialogBuilder.dismiss();
                }
            });

            dialogBuilder.setView(dialogView);
            dialogBuilder.show();
        }
    };

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Subject currnetSubject = (Subject) parent.getItemAtPosition(position);

            Intent i = new Intent(ShowListSubjectActivity.this, DetailASubject.class);
            i.putExtra("subjectCode", currnetSubject.getSubjectCode());
            i.putExtra("subjectName", currnetSubject.getSubjectName());
            i.putExtra("subjectCredits", currnetSubject.getSubjectCredits());
            i.putExtra("subjectHours", currnetSubject.getSubjectHours());
            i.putExtra("subjectCoefficient", currnetSubject.getSubjectCoefficient());
            startActivity(i);
        }
    };

    SearchView.OnQueryTextListener searchViewQueryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            search(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            //search(newText);
            return true;
        }
    };
    SearchView.OnCloseListener closeListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            mList.setAdapter(mAdapter);
            System.out.println("Close GGGG");
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        getSubjects();
    }

    private void addSubject(Subject subject){
        mFirebaseDatabase.child(subject.getSubjectCode()).setValue(subject)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ShowListSubjectActivity.this, "Inserting data is success", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void getSubjects(){
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListSubject.clear();
                for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){
                    Subject subject = subjectSnapshot.getValue(Subject.class);
                    mListSubject.add(subject);
                }
                mAdapter = new SubjectAdapter(ShowListSubjectActivity.this, mListSubject);
                mList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getSubjectName(){
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Subjects");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListSubjectName.clear();
                for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){
                    Subject subject = subjectSnapshot.getValue(Subject.class);
                    Log.d("gggggggg", subject.getSubjectName());
                    mListSubjectName.add(subject.getSubjectName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static Subject getSubject(String subjectName){
        for(int i = 0; i < mListSubject.size(); i++){
            if(mListSubject.get(i).getSubjectName().equals(subjectName)){
                return mListSubject.get(i);
            }
        }
        return null;
    }

    /** So hard */
    private void search(final String subjectName){
        System.out.println(subjectName);
        final List<Teacher> listResult = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Departments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot departmentSnapshot : dataSnapshot.getChildren()){
                    final Department currentDepatment = departmentSnapshot.getValue(Department.class);
                    System.out.println(currentDepatment.getDepartmentCode());


                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Departments")
                            .child(currentDepatment.getDepartmentCode())
                            .child("Teachers");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot teacherSnapshot : dataSnapshot.getChildren()){
                                final Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                System.out.println(teacher.getFullName());


                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Departments")
                                        .child(currentDepatment.getDepartmentCode())
                                        .child("Teachers")
                                        .child(teacher.getTeachCode())
                                        .child("subjects");

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){
                                            Subject subject = subjectSnapshot.getValue(Subject.class);

                                            if(subjectName.equals(subject.getSubjectName())){
                                                System.out.println(subjectName == subject.getSubjectName());
                                                listResult.add(teacher);
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
                TeacherAdapter teacherAdapter = new TeacherAdapter(ShowListSubjectActivity.this, listResult);
                mList.setAdapter(teacherAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
