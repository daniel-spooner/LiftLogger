package com.doony.liftlogger;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class DaySetListAdapter extends RecyclerView.Adapter<DaySetListAdapter.DaySetViewHolder> {
    Context context;
    ArrayList<Calendar> dateList;
    int currentYear, currentMonth, currentDate;
    String exerciseID;

    public DaySetListAdapter(Context context, ArrayList dateList, String exerciseID) {
        this.context = context;
        this.dateList = dateList;
        this.exerciseID = exerciseID;

        Calendar cal = Calendar.getInstance();

        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);
        currentDate = cal.get(Calendar.DATE);
    }

    @NonNull
    @Override
    public DaySetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.day_set_item, parent, false);
//        Log.d("DaySetListAdapter", "CreateViewHolder");
//        DaySetViewHolder viewHolder = new DaySetListAdapter.DaySetViewHolder(view);
//        viewHolder.setIsRecyclable(false);
//        return viewHolder;
        return new DaySetListAdapter.DaySetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaySetViewHolder holder, int position) {
        String timeText;
        timeText = (String) DateFormat.format("MMMM dd yyyy", dateList.get(position).getTimeInMillis());

        holder.tvTime.setText(timeText);
//        holder.setIsRecyclable(false);

        Calendar cal = dateList.get(holder.getAdapterPosition());
        if(currentYear == cal.get(Calendar.YEAR) && currentMonth == cal.get(Calendar.MONTH) && currentDate == cal.get(Calendar.DATE)) {
            holder.expandList(context, exerciseID, dateList.get(holder.getAdapterPosition()));
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.getIsExpanded()) {
                    holder.collapseList();
                } else {
                    holder.expandList(context, exerciseID, dateList.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public static class DaySetViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        ImageView ivDropDown;
        RecyclerView rvSets;
        ConstraintLayout mainLayout;

        ArrayList<SetListItem> setList;
        boolean isExpanded;

        public DaySetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.textViewSetDate);
            ivDropDown = itemView.findViewById(R.id.imageViewDropDown);
            rvSets = itemView.findViewById(R.id.recyclerViewSetList);
            mainLayout = itemView.findViewById(R.id.mainLayout);

            setList = new ArrayList<>();
            isExpanded = false;
        }

        public boolean getIsExpanded() {
            return isExpanded;
        }

        public void expandList(Context context, String exerciseID, Calendar calendarStart) {
            rvSets.setVisibility(View.VISIBLE);
            ivDropDown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

            isExpanded = true;

            Calendar calendarEnd = (Calendar)calendarStart.clone();
            calendarEnd.add(Calendar.DATE, 1);

            long startTime = calendarStart.getTimeInMillis();
            long endTime = calendarEnd.getTimeInMillis();

            ExerciseDatabaseHelper exerciseDB = new ExerciseDatabaseHelper(context);
            Cursor cursor = exerciseDB.readExerciseSetsBetweenTimestamps(exerciseID, startTime, endTime);

            setList.clear();

            if(cursor.getCount() == 0) {
                //Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    setList.add(new SetListItem(
                            cursor.getString(0), cursor.getString(1), cursor.getString(3),
                            cursor.getString(4), cursor.getInt(5)
                    )); // 0: id, 1: time, 3: weight, 4: reps, 5: favourite
                }
            }

            String exerciseName = exerciseDB.getNameFromID(exerciseID);
            SetListAdapter adapter = new SetListAdapter(context, setList, exerciseName);
            rvSets.setAdapter(adapter);
            rvSets.setLayoutManager(new LinearLayoutManager(context));
        }

        public void collapseList() {
            rvSets.setVisibility(View.GONE);
            ivDropDown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);

            isExpanded = false;
        }
    }
}
