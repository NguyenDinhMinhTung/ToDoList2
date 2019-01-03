package com.example.megas.todolist2.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListAdapterViewHolder> {
    List<EventDTO> eventDTOList;
    Context context;

    EventsDAO eventsDAO;

    boolean isFirst = false;

    public EventListAdapter(Context context, List<EventDTO> eventDTOList) {
        this.eventDTOList = eventDTOList;
        this.context = context;

        eventsDAO = new EventsDAO(context);

        if (eventDTOList.get(0).getType() == -1) {
            isFirst = true;
            eventDTOList.remove(0);
        }
    }

    @NonNull
    @Override
    public EventListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.layout_event_item, viewGroup, false);

        return new EventListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventListAdapterViewHolder eventListAdapterViewHolder, int i) {
        final EventDTO eventDTO = eventDTOList.get(i);

        eventListAdapterViewHolder.layout.setBackgroundColor(Ulti.colors[eventDTO.getColor()]);

        eventListAdapterViewHolder.txtEventName.setText(eventDTO.getEventName());
        eventListAdapterViewHolder.txtComment.setText(eventDTO.getComment());

        long dayRemaining;
        String remaining = "";

        if (isFirst) {
            Date date1 = _Date.Parse(eventDTO.getDaytime()).toDate();
            Date date2 = Calendar.getInstance().getTime();
            long diff = date1.getTime() - date2.getTime();
            dayRemaining = (long) (diff / (1000.0 * 60 * 60 * 24));

            if (dayRemaining > 0) {
                remaining = ("（後" + dayRemaining + "日）");
            } else {
                remaining = "（今日）";
            }
        }

        if (eventDTO.getType() == 1) {
            eventListAdapterViewHolder.txtTime.setText("全日" + remaining);
        } else {
            eventListAdapterViewHolder.txtTime.setText(_Date.Parse(eventDTO.getDaytime()).toTimeString() + remaining);
        }

        eventListAdapterViewHolder.checkBox.setChecked(eventDTO.getStatus() == 1);
        if (eventDTO.getStatus() == 1) {
            setPaintFlags(eventListAdapterViewHolder, Paint.STRIKE_THRU_TEXT_FLAG);
        }

        eventListAdapterViewHolder.btnShowPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, eventListAdapterViewHolder.btnShowPopupMenu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_event_item, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("編集")) {
                            Intent intent = new Intent(context, AddEventActivity.class);
                            intent.putExtra("EventDTO", eventDTO);

                            context.startActivity(intent);
                        } else if (menuItem.getTitle().equals("削除")) {
                            eventsDAO.delete(eventDTO.getId());
                            eventDTOList.remove(eventDTO);

                            notifyDataSetChanged();
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

        eventListAdapterViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    setPaintFlags(eventListAdapterViewHolder, Paint.STRIKE_THRU_TEXT_FLAG);

                    eventsDAO.updateStatus(eventDTO.getId(), 1);
                } else {
                    setPaintFlags(eventListAdapterViewHolder, 0);

                    eventsDAO.updateStatus(eventDTO.getId(), 0);
                }
            }
        });
    }

    private void setPaintFlags(EventListAdapterViewHolder mainListAdapterViewHolder, int flags) {
        mainListAdapterViewHolder.txtEventName.setPaintFlags(flags);
        mainListAdapterViewHolder.txtComment.setPaintFlags(flags);
        mainListAdapterViewHolder.txtTime.setPaintFlags(flags);
    }

    @Override
    public int getItemCount() {
        return eventDTOList.size();
    }

    public class EventListAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtEventName, txtTime, txtComment;
        CheckBox checkBox;
        ImageButton btnShowPopupMenu;
        CardView layout;

        View view;

        public EventListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            this.view = itemView;

            layout = itemView.findViewById(R.id.layoutMainList);

            txtEventName = itemView.findViewById(R.id.txtEventNameMainList);
            txtTime = itemView.findViewById(R.id.txtTimeMainList);
            txtComment = itemView.findViewById(R.id.txtCommentMainList);

            checkBox = itemView.findViewById(R.id.ckbMainList);
            btnShowPopupMenu = itemView.findViewById(R.id.btnShowMenuMainList);
        }
    }
}
