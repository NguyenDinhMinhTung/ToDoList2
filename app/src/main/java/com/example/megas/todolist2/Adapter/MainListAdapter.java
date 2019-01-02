package com.example.megas.todolist2.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.megas.todolist2.DTO.EventDTO;
import com.example.megas.todolist2.R;

import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainListAdapterViewHolder> {
    List<EventDTO> eventDTOList;

    public MainListAdapter(List<EventDTO> eventDTOList) {
        this.eventDTOList = eventDTOList;
    }

    @NonNull
    @Override
    public MainListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view=layoutInflater.inflate(R.layout.layout_event_item,viewGroup,false);

        return new MainListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListAdapterViewHolder mainListAdapterViewHolder, int i) {
        EventDTO eventDTO=eventDTOList.get(i);

        mainListAdapterViewHolder.txtEventName.setText(eventDTO.getEventName());
        mainListAdapterViewHolder.txtComment.setText(eventDTO.getComment());
        mainListAdapterViewHolder.txtTime.setText(eventDTO.getDaytime());
        mainListAdapterViewHolder.checkBox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return eventDTOList.size();
    }

    public class MainListAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtEventName, txtTime, txtComment;
        CheckBox checkBox;
        ImageButton btnShowPopupMenu;

        public MainListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            txtEventName = itemView.findViewById(R.id.txtEventNameMainList);
            txtTime = itemView.findViewById(R.id.txtTimeMainList);
            txtComment = itemView.findViewById(R.id.txtCommentMainList);

            checkBox = itemView.findViewById(R.id.ckbMainList);
            btnShowPopupMenu = itemView.findViewById(R.id.btnShowMenuMainList);
        }
    }
}
