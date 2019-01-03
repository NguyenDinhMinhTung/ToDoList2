package com.example.megas.todolist2.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.megas.todolist2.AddEventActivity;
import com.example.megas.todolist2.DAO.EventsDAO;
import com.example.megas.todolist2.DTO.EventDTO;
import com.example.megas.todolist2.R;
import com.example.megas.todolist2.Ulti;
import com.example.megas.todolist2._Date;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainListAdapterViewHolder> {
    List<List<EventDTO>> eventDTOList;
    Context context;

    public MainListAdapter(Context context, List<List<EventDTO>> eventDTOList) {
        this.eventDTOList = eventDTOList;
        this.context = context;
    }

    @NonNull
    @Override
    public MainListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.layout_day_item, viewGroup, false);

        return new MainListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainListAdapterViewHolder mainListAdapterViewHolder, int i) {
        List<EventDTO> list = eventDTOList.get(i);

        _Date now = new _Date();
        _Date date;
        if (list.size() > 0) {
            date = Ulti.addDay(_Date.Parse(list.get(0).getDaytime()), -list.get(0).getNotiday());

            if (date.toDateString().compareTo(now.toDateString()) < 0) {
                date = now;
            }
        } else {
            date = now;
        }

        mainListAdapterViewHolder.txtDay.setText(String.format("%d月%d日", date.getMonth(), date.getDay()));

        EventListAdapter eventListAdapter = new EventListAdapter(context, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mainListAdapterViewHolder.lstEvent.setAdapter(eventListAdapter);
        mainListAdapterViewHolder.lstEvent.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return eventDTOList.size();
    }

    public class MainListAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtDay;
        RecyclerView lstEvent;

        public MainListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDay = itemView.findViewById(R.id.txtDayDayItem);
            lstEvent = itemView.findViewById(R.id.lstEventDayItem);
        }
    }
}
