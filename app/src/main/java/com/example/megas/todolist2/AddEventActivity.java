package com.example.megas.todolist2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.megas.todolist2.DAO.EventsDAO;
import com.example.megas.todolist2.DTO.EventDTO;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtEventName, txtComment, txtNotifyDay;
    TextView btnDate, btnTime;
    CheckBox ckbAddDay;
    Spinner spnColor;

    Button btnAdd, btnCancel;

    EventsDAO eventsDAO;

    EventDTO eventDTO;

    _Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Event");

        setVar();

        setEvent();
    }

    private void setVar() {
        txtEventName = findViewById(R.id.txtEventNameAddEvent);
        txtComment = findViewById(R.id.txtCommentAddEvent);
        txtNotifyDay = findViewById(R.id.txtNotifyAddEvent);

        btnDate = findViewById(R.id.btnDateAddEvent);
        btnTime = findViewById(R.id.btnTimeAddEven);

        ckbAddDay = findViewById(R.id.ckbAllDayAddEvent);

        spnColor = findViewById(R.id.spnColorAddEvent);

        btnAdd = findViewById(R.id.btnAddAddEvent);
        btnCancel = findViewById(R.id.btnCancelAddEvent);

        eventsDAO = new EventsDAO(this);

        eventDTO = (EventDTO) getIntent().getSerializableExtra("EventDTO");

        date = new _Date();

        btnDate.setText(dateToDateString(date));
        btnTime.setText(dateToTimeString(date));
    }

    private void setEvent() {
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);

        ckbAddDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    btnTime.setEnabled(false);
                } else
                    btnTime.setEnabled(true);
            }
        });

        String[] color = {"White", "Red", "Green", "Yellow", "Magenta"};
        ArrayAdapter spnColorAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, color);
        spnColorAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnColor.setAdapter(spnColorAdapter);

        if (eventDTO != null) {
            txtEventName.setText(eventDTO.getEventName());
            txtNotifyDay.setText(String.valueOf(eventDTO.getNotiday()));
            txtComment.setText(eventDTO.getComment());

            ckbAddDay.setChecked(eventDTO.getStatus() == 1);

            date = _Date.Parse(eventDTO.getDaytime());

            spnColor.setSelection(eventDTO.getColor());

            btnDate.setText(dateToDateString(date));
            btnTime.setText(dateToTimeString(date));
        }
    }

    private String dateToDateString(_Date date) {
        return date.getMonth() + "月" + date.getDay() + "日";
    }

    private String dateToTimeString(_Date date) {
        return date.getHour() + "時" + date.getMinute() + "分";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.btnAddAddEvent:
                if (txtEventName.getText().toString().isEmpty()) {
                    Toast.makeText(this, "タイトルを入力してください", Toast.LENGTH_SHORT).show();

                } else if (!txtNotifyDay.getText().toString().matches("[0-9]")) {
                    Toast.makeText(this, "通知を正しく入力してください", Toast.LENGTH_SHORT).show();

                } else {
                    if (ckbAddDay.isChecked()) {
                        date.setHour(23);
                        date.setMinute(59);
                        date.setSecond(59);
                    }
                    String evenName = txtEventName.getText().toString();
                    String daytime = date.toString();
                    String comment = txtComment.getText().toString();

                    int notifyDay = Integer.parseInt(txtNotifyDay.getText().toString());
                    int color = spnColor.getSelectedItemPosition();
                    int type = ckbAddDay.isChecked() ? 1 : 0;
                    int objectId = 0;

                    int id;

                    if (eventDTO == null) {
                        eventDTO = new EventDTO(0, -1, type, daytime, notifyDay, 0, color, objectId, evenName, comment);
                        id = eventsDAO.addEvent(eventDTO);

                    } else {
                        id = eventDTO.getId();

                        eventDTO.setType(type);
                        eventDTO.setDaytime(daytime);
                        eventDTO.setNotiday(notifyDay);
                        eventDTO.setColor(color);
                        eventDTO.setObjectId(objectId);
                        eventDTO.setEventName(evenName);
                        eventDTO.setComment(comment);

                        eventsDAO.update(eventDTO);
                    }

                    Sync.PushToSyncQueue(this, id, 1);
                    Sync.StartSyncToServer(this);

                    Ulti.pushNotification(this);
                    finish();
                }
                break;

            case R.id.btnCancelAddEvent:
                finish();
                break;

            case R.id.btnDateAddEvent:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date.setYear(year);
                        date.setMonth(month + 1); //month bat dau tu 0
                        date.setDay(day);

                        btnDate.setText(dateToDateString(date));
                    }
                }, date.getYear(), date.getMonth() - 1, date.getDay());

                datePickerDialog.show();
                break;

            case R.id.btnTimeAddEven:
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        date.setHour(hour);
                        date.setMinute(min);

                        btnTime.setText(dateToTimeString(date));
                    }
                }, date.getHour(), date.getMinute(), true);

                timePickerDialog.show();
                break;
        }
    }
}
