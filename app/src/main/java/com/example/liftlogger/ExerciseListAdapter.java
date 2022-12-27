package com.example.liftlogger;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.MyViewHolder> {

    Context context;
    ArrayList exerciseIDs, exerciseNames;

    public ExerciseListAdapter(Context context, ArrayList exerciseIDs, ArrayList exerciseNames) {
        this.context = context;
        this.exerciseIDs = exerciseIDs;
        this.exerciseNames = exerciseNames;
    }

    @NonNull
    @Override
    public ExerciseListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.exercise_item, parent, false);
        return new ExerciseListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExerciseListAdapter.MyViewHolder holder, final int position) {
        holder.tvName.setText(String.valueOf(exerciseNames.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewExercise.class);
                intent.putExtra("ID", String.valueOf(exerciseIDs.get(holder.getAdapterPosition())));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseNames.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewExerciseItemName);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
