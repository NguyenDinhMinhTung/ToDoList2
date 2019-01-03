package com.example.megas.todolist2;

import java.util.Calendar;
import java.util.Date;

public class _Date {
    private int year, month, day, hour, minute, second;

    public _Date() {
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; //month bat dau tu 0
        day = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
    }

    public _Date(Date date) {
        this.year = date.getYear()+1900;
        this.month = date.getMonth()+1;
        this.day = date.getDate();

        this.hour = date.getHours();
        this.minute = date.getMinutes();
        this.second = date.getSeconds();
    }

    public _Date(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return year + "-" + Ulti.numToString(month, 2) + "-" + Ulti.numToString(day, 2)
                + " " + Ulti.numToString(hour, 2) + ":" + Ulti.numToString(minute, 2) + ":" + Ulti.numToString(second, 2);
    }

    public Date toDate() {
        return new Date(year - 1900, month - 1, day, hour, minute, second);
    }

    public static _Date Parse(String date) {
        String s1[] = date.split(" ");

        String yearmonthday[] = s1[0].split("-");
        String hourminsecond[] = s1[1].split(":");

        int year = Integer.parseInt(yearmonthday[0]);
        int month = Integer.parseInt(yearmonthday[1]);
        int day = Integer.parseInt(yearmonthday[2]);

        int hour = Integer.parseInt(hourminsecond[0]);
        int minute = Integer.parseInt(hourminsecond[1]);
        int second = Integer.parseInt(hourminsecond[2]);

        return new _Date(year, month, day, hour, minute, second);
    }

    public String toTimeString() {
        return Ulti.numToString(hour, 2) + ":" + Ulti.numToString(minute, 2);
    }

    public String toDateString() {
        return year + "-" + Ulti.numToString(month, 2) + "-" + Ulti.numToString(day, 2);
    }

    public int compareTo(_Date date) {
        return toString().compareTo(date.toString());
    }
}
