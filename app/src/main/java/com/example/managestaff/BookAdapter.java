package com.example.managestaff;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import user.Book;

public class BookAdapter extends ArrayAdapter<Book> {

    private List<Book> mList;
    private Context context;

    public BookAdapter(@NonNull Context context, @NonNull List<Book> list) {
        super(context, R.layout.book_list_item, list);
        this.context = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView  = layoutInflater.inflate(R.layout.book_list_item, null, false);

        TextView sttView = itemView.findViewById(R.id.stt);
        TextView bookNameView = itemView.findViewById(R.id.book_name_view);
        final TextView totalUsedView = itemView.findViewById(R.id.used_view);
        final TextView subjectUsedView = itemView.findViewById(R.id.subjects_use_view);

        final Book currentBook = mList.get(position);

        sttView.setText("" + position);
        bookNameView.setText(currentBook.getBookName());
        //System.out.println("GG " + currentBook.getBookName());


        /**
         * aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
         * */
        final List<String> result = new LinkedList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Books");

        databaseReference.child(currentBook.getBookName()).child("SubjectsUsage")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot bookSnapshot : dataSnapshot.getChildren()){
                            System.out.println("key: " + bookSnapshot.getKey());
                            result.add(bookSnapshot.getKey());
                        }

                        //System.out.println(currentBook.getBookName() +" size =  " + result.size());
                        totalUsedView.setText("" + result.size());

                        StringBuilder tmp = new StringBuilder();
                        for (int i = 0; i < result.size(); i ++){
                            tmp.append(result.get(i)).append(", ");
                        }

                        //System.out.println("GG  " + tmp);
                        subjectUsedView.setText(tmp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        return itemView;
    }

    public static List<String> getSubjectsUsed(final Book book){
        final List<String> result = new LinkedList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Books");

        databaseReference.child(book.getBookName()).child("SubjectsUsage")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot bookSnapshot : dataSnapshot.getChildren()){
                            System.out.println("key: " + bookSnapshot.getKey());
                            result.add(bookSnapshot.getKey());
                        }

                        System.out.println(book.getBookName() +" size =  " + result.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        System.out.println(book.getBookName() +" size =  " + result.size());

        return result;
    }
}
