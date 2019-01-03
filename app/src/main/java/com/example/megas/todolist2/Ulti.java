package com.example.megas.todolist2;

import android.graphics.Color;

import java.util.Calendar;
import java.util.Date;

public class Ulti {
    public static int[] colors = {Color.rgb(255, 255, 255),
            Color.rgb(255, 112, 67),
            Color.rgb(156, 204, 101),
            Color.rgb(253, 216, 53),
            Color.rgb(186, 104, 200)};

    public static String numToString(int num, int length) {
        String result = String.valueOf(num);

        for (int i = 0; i < length - result.length(); i++) {
            result = "0" + result;
        }

        return result;
    }

    public static _Date addDay(_Date date, int day) {
        Date tmp = date.toDate();
        Calendar c = Calendar.getInstance();
        c.setTime(tmp);
        c.add(Calendar.DATE, day);
        return new _Date(c.getTime());
    }
}
