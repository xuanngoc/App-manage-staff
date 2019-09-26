package com.example.managestaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import user.Book;
import user.Department;

public class ShowListBookActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton fabAddBook;
    private TextView subjectUseView;

    private static DatabaseReference mFirebaseDatabase;
    private static FirebaseDatabase mFirebaseInstance;

    public static List<String> listBookName;
    private static List<Book> mList;
    private BookAdapter mAdapter;

    private static  int[] useBookList = {0, 0};;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_book);

        listView = findViewById(R.id.list_book_view);
        fabAddBook = findViewById(R.id.fab_add_book);
        subjectUseView = findViewById(R.id.subject_use_view);

        listBookName = new LinkedList<>();
        mList = new ArrayList<>();




        mAdapter = new BookAdapter(this, mList);
        listView.setAdapter(mAdapter);


        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Books");

        fabAddBook.setOnClickListener(fabAddBookClickListener);
        subjectUseView.setOnClickListener(btnSubjectUseViewClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getListBookName();
        getListBook();
    }

    View.OnClickListener fabAddBookClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog dialogBuilder = new AlertDialog.Builder(ShowListBookActivity.this).create();
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog_add_book, null);

            final TextInputEditText bookNameEditText =  dialogView.findViewById(R.id.book_code_edit_text);
            final TextInputEditText bookAuthorEditText = dialogView.findViewById(R.id.book_name_edit_text);
            MaterialButton btnAdd = dialogView.findViewById(R.id.btn_add_a_book);
            MaterialButton btnCancel = dialogView.findViewById(R.id.btn_cancel_add_book);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder.dismiss();
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Book book = new Book(bookNameEditText.getText().toString(),
                            bookAuthorEditText.getText().toString());
                    addBook(book);

                    dialogBuilder.dismiss();
                }
            });

            dialogBuilder.setView(dialogView);
            dialogBuilder.show();
        }
    };
    View.OnClickListener btnSubjectUseViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(ShowListBookActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            //sortByUsedBook();
        }
    };

    private void addBook(final Book book){
        mFirebaseDatabase.child(book.getBookName()).setValue(book)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ShowListBookActivity.this, "Thêm sách thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListBook(){
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for(DataSnapshot bookSnapshot : dataSnapshot.getChildren()){
                    Book book = bookSnapshot.getValue(Book.class);
                    mList.add(book);
                }
                mAdapter = new BookAdapter(ShowListBookActivity.this, mList);
                listView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private static void sortByUsedBook(){

        Collections.sort(mList, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return 0;
            }
        });


        /*mList.sort(new Comparator<Book>() {
            @Override
            public int compare(final Book o1, Book o2) {

                useBookList[0] = 0;
                useBookList[1] = 0;
                System.out.println("Start");
                mFirebaseDatabase.child(o1.getBookName()).child("SubjectsUsage").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot bookSnapshot : dataSnapshot.getChildren()){
                            System.out.println(o1.getBookName() + "  " + bookSnapshot.getKey());
                            useBookList[0] ++;
                            Log.d("useBook1:  ", useBookList[0]+ "");

                        }
                        Log.d("useBook1:  ", useBookList[0]+ "");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                mFirebaseDatabase.child(o2.getBookName()).child("SubjectsUsage").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot bookSnapshot : dataSnapshot.getChildren()){
                            //bookSnapshot.getKey();
                            useBookList[1] ++;

                        }
                        Log.d("useBook2:  ", useBookList[1]+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });



                //Log.d("GG: ","" + useBookList + "  " + useBook2);
                return Integer.compare(useBookList[0], useBookList[1]);

            }
        });*/
    }

    public void getListBookName(){
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listBookName.clear();
                for(DataSnapshot bookSnapshot : dataSnapshot.getChildren()){
                    Book book = bookSnapshot.getValue(Book.class);
                    listBookName.add(book.getBookName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static Book getBook(String bookName){
        for(int i=0; i< mList.size(); i++){
            if(mList.get(i).getBookName().equals(bookName)){
                return mList.get(i);
            }
        }
        return null;
    }





}
