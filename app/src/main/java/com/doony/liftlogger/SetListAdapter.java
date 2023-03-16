package com.doony.liftlogger;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class SetListAdapter extends RecyclerView.Adapter<SetListAdapter.SetViewHolder> {

    Context context;
    ArrayList<SetListItem> setList;
    String exerciseName;

    public SetListAdapter(Context context, ArrayList setList, String exerciseName) {
        this.context = context;

        this.setList = setList;
        this.exerciseName = exerciseName;
    }

    @NonNull
    @Override
    public SetListAdapter.SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.set_item, parent, false);
        return new SetListAdapter.SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SetListAdapter.SetViewHolder holder, final int position) {


        String timeText, weightText, repetitionText;

        timeText = String.valueOf(setList.get(position).getTime());
        weightText = String.valueOf(setList.get(position).getWeight()) + " lbs";
        repetitionText = String.valueOf(setList.get(position).getRepetitions()) + " reps";

        Date date = new Date(Long.parseLong(timeText));

        timeText = (String) DateFormat.format("hh:mm aa", Long.parseLong(timeText));

        holder.tvTime.setText(timeText);
        holder.tvWeight.setText(weightText);
        holder.tvRepetitions.setText(repetitionText);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditSet.class);
                intent.putExtra("ID", String.valueOf(setList.get(holder.getAdapterPosition()).getID()));
                intent.putExtra("NAME", exerciseName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return setList.size();
    }

    public static class SetViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        TextView tvWeight;
        TextView tvRepetitions;
        ConstraintLayout mainLayout;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.textViewSetItemTime);
            tvWeight = itemView.findViewById(R.id.textViewSetItemWeight);
            tvRepetitions = itemView.findViewById(R.id.textViewSetItemReps);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
