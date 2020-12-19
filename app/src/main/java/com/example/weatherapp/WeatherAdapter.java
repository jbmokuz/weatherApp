package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private String[] weather_states;

    public void setClickListener(MainActivity mainActivity) {
        System.out.println("click");
    }

    public interface ItemClickListener {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    public WeatherAdapter (String[] w){
        weather_states = w;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_layout, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        holder.tv.setText(weather_states[position]);
    }

    @Override
    public int getItemCount() {
        return weather_states.length;
    }


}