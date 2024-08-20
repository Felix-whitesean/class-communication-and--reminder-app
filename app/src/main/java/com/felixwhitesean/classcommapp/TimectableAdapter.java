package com.felixwhitesean.classcommapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TimectableAdapter extends RecyclerView.Adapter<TimectableAdapter.ViewHolder> {

        private ArrayList<TimetableModelClass> modelClassesRows;
        private Context context;
//        public GridAdapter(List<String> data) {
//            this.data = data;
//        }
        public TimectableAdapter(Context context){
            this.context = context;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.timetable_recycler_view, parent, false); // Use your item layout
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // Bind data to views in the item layout
            TimetableModelClass currentTimetable = modelClassesRows.get(position);
            holder.session1.setText(modelClassesRows.get(position).getD_o_w());
            holder.session2.setText(modelClassesRows.get(position).getLesson_1());
            holder.session3.setText(modelClassesRows.get(position).getLesson_2());
            holder.session4.setText(modelClassesRows.get(position).getLesson_3());
            holder.session5.setText(modelClassesRows.get(position).getLesson_4());
            holder.session6.setText(modelClassesRows.get(position).getLesson_5());
            holder.session2Class.setText(modelClassesRows.get(position).getSession1_room());
            holder.session3Class.setText(modelClassesRows.get(position).getSession2_room());
            holder.session4Class.setText(modelClassesRows.get(position).getSession3_room());
            holder.session5Class.setText(modelClassesRows.get(position).getSession4_room());
            holder.session6Class.setText(modelClassesRows.get(position).getSession5_room());
        }

        @Override
        public int getItemCount() {
            return modelClassesRows.size();
        }

        public void setModelClassesRows(ArrayList<TimetableModelClass> lesson){
            this.modelClassesRows = lesson;
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView session1, session2, session3, session4, session5, session6, session2Class, session3Class, session4Class, session5Class, session6Class;
            private LinearLayout timetable_container;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                session1 = itemView.findViewById(R.id.session1);
                session2 = itemView.findViewById(R.id.session2);
                session3 = itemView.findViewById(R.id.session3);
                session4 = itemView.findViewById(R.id.session4);
                session5 = itemView.findViewById(R.id.session5);
                session6 = itemView.findViewById(R.id.session6);
                session2Class = itemView.findViewById(R.id.session2_class);
                session3Class = itemView.findViewById(R.id.session3_class);
                session4Class = itemView.findViewById(R.id.session4_class);
                session5Class = itemView.findViewById(R.id.session5_class);
                session6Class = itemView.findViewById(R.id.session6_class);
                timetable_container = itemView.findViewById(R.id.timetable_container);
            }
        }
}
