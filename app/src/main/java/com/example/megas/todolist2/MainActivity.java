package com.example.megas.todolist2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.example.megas.todolist2.Adapter.MainListAdapter;
import com.example.megas.todolist2.DAO.EventsDAO;
import com.example.megas.todolist2.DTO.EventDTO;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    RecyclerView lstMainList;
    MainListAdapter mainListAdapter;

    EventsDAO eventsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setVar();

        //eventsDAO.addEvent(new EventDTO(0, 0, 0, Calendar.getInstance().getTime().toString(), 0, 0, 0, 0, "dddd", "fff"));

        generateList();
    }

    private void generateList() {
        mainListAdapter = new MainListAdapter(eventsDAO.getListEvent());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lstMainList.setAdapter(mainListAdapter);
        lstMainList.setLayoutManager(layoutManager);
    }

    public void setVar() {
        lstMainList = findViewById(R.id.lstMainList);

        eventsDAO = new EventsDAO(this);
    }
}
