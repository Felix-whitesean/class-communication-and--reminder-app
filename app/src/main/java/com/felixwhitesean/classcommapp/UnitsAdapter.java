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

public class UnitsAdapter extends RecyclerView.Adapter<UnitsAdapter.ViewHolder> {
    private Context context2;
    private ArrayList<UnitsModelClass> Units;
    public UnitsAdapter(Context context2) {
        this.context2 = context2;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.units_recycler_view, parent, false); // Use your item layout
        return new UnitsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to views in the item layout
        UnitsModelClass units = Units.get(position);
        holder.serial_no.setText(Units.get(position).getSerial_no());
        holder.unit_name.setText(Units.get(position).getUnit_name());
        holder.unit_code.setText(Units.get(position).getUnit_code());
        holder.lecturer.setText(Units.get(position).getLecturer());
    }

    @Override
    public int getItemCount() {
        return Units.size();
    }

    public void setUnits(ArrayList<UnitsModelClass> unitDetails){
        this.Units = unitDetails;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView serial_no, unit_name, unit_code, lecturer;
        private LinearLayout timetable_container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serial_no = itemView.findViewById(R.id.serialNo);
            unit_name = itemView.findViewById(R.id.unitName);
            unit_code = itemView.findViewById(R.id.unitCode);
            lecturer = itemView.findViewById(R.id.lecturerName);
        }
    }
}
