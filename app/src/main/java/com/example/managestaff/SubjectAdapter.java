package com.example.managestaff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import user.Department;
import user.Subject;

public class SubjectAdapter extends ArrayAdapter<Subject> {

    private List<Subject>  list;
    private Context context;

    public SubjectAdapter(Context context, List<Subject> listSubject) {
        super(context, R.layout.subject_list_item, listSubject);
        this.context = context;
        this.list = listSubject;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View listItemView = layoutInflater.inflate(R.layout.subject_list_item, null, false);




        TextView subjectCodeView = listItemView.findViewById(R.id.subject_code_view);
        TextView subjectNameView = listItemView.findViewById(R.id.subject_name_view);

        Subject subject = list.get(position);

        subjectCodeView.setText(subject.getSubjectCode());
        subjectNameView.setText(subject.getSubjectName());


        return listItemView;
    }
}
