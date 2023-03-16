package com.doony.liftlogger;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

    Context context;
    ArrayList<ExerciseListItem> exerciseList;

    public ExerciseListAdapter(Context context, ArrayList exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExerciseViewHolder holder, final int position) {
        holder.tvName.setText(String.valueOf(exerciseList.get(position).getName()));
        int imageResource = exerciseList.get(position).getFavourite() == 1 ? R.drawable.ic_baseline_star_24 : R.drawable.ic_baseline_star_outline_24;
        holder.btnFavourite.setImageResource(imageResource);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewExercise.class);
                intent.putExtra("ID", String.valueOf(exerciseList.get(holder.getAdapterPosition()).getID()));
                context.startActivity(intent);
            }
        });
        holder.btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = String.valueOf(exerciseList.get(holder.getAdapterPosition()).getID());
                ExerciseDatabaseHelper db = new ExerciseDatabaseHelper(context);
                boolean setFavourite = true;
                if(db.getFavouriteExercise(id)) {
                    setFavourite = false;
                }
                db.setFavouriteExercise(id, setFavourite);

                ImageView imageView = (ImageView) view;
                if(setFavourite) {
                    imageView.setImageResource(R.drawable.ic_baseline_star_24);
                } else {
                    imageView.setImageResource(R.drawable.ic_baseline_star_outline_24);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView btnFavourite;
        ConstraintLayout mainLayout;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewExerciseItemName);
            btnFavourite = itemView.findViewById(R.id.buttonFavourite);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
