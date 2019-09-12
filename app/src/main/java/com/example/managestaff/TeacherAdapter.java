package com.example.managestaff;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import user.Teacher;

public class TeacherAdapter  extends ArrayAdapter<Teacher> {

    private static final String TAG = "TeacherAdapter";

    private List<Teacher> mList;
    private Context context;


    public TeacherAdapter(Context context, List<Teacher> list) {
        super(context, R.layout.teacher_list_item, list);
        this.context = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View listItemView = layoutInflater.inflate(R.layout.teacher_list_item, null, false);

        TextView teacherCodeView = listItemView.findViewById(R.id.teacher_code_view);
        TextView fullNameView = listItemView.findViewById(R.id.full_name_view);

        Teacher teacher = mList.get(position);

        teacherCodeView.setText(teacher.getTeachCode());
        fullNameView.setText(teacher.getFullName());

        return listItemView;
    }
}
