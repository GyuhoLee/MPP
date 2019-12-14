package com.example.gradenotifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private ArrayList<ItemObject> mList;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_subject, textView_grade;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_subject = (TextView) itemView.findViewById(R.id.subject);
            textView_grade = (TextView) itemView.findViewById(R.id.grade);
        }
    }

    //생성자
    public MyAdapter(ArrayList<ItemObject> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

        holder.textView_subject.setText(String.valueOf(mList.get(position).getSubject()));
        holder.textView_grade.setText(String.valueOf(mList.get(position).getGrade()));
        //다 해줬는데도 GlideApp 에러가 나면 rebuild project를 해주자.
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
