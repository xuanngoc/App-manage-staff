package com.example.managestaff;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import user.Teacher;

public class TeacherAdapter  extends RecyclerView.Adapter<TeacherAdapter.ViewHolder>{

    private static final String TAG = "TeacherAdapter";

    private List<Teacher> mList;
    private OnTeacherListener mOnTeacherListenter;

    public TeacherAdapter(List<Teacher> mList, OnTeacherListener onTeacherListener) {
        this.mList = mList;
        this.mOnTeacherListenter = onTeacherListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_list_item, parent, false);

        //Log.d("Teacher Adapter", "on Create View Holder");

        return new ViewHolder(itemView, mOnTeacherListenter);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Teacher teacher = mList.get(position);
        holder.teacherCodeView.setText(teacher.getTeachCode());
        holder.fullNameView.setText(teacher.getFullName());

        //Log.d("Teacher Adapter", "on BindViewHolder");
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RelativeLayout parentLayout;
        ImageView imageView;
        TextView teacherCodeView;
        TextView fullNameView;

        OnTeacherListener onTeacherListener;

        public ViewHolder(View itemView, OnTeacherListener onTeacherListener) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parent_layout);
            imageView = itemView.findViewById(R.id.imageAvatar);
            teacherCodeView = itemView.findViewById(R.id.teacher_code_view);
            fullNameView = itemView.findViewById(R.id.full_name_view);

            this.onTeacherListener = onTeacherListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onTeacherListener.onTeacherClick(getAdapterPosition());
        }
    }

    public interface OnTeacherListener{
        void onTeacherClick(int position);
    }

}
